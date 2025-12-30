package com.example.demo.service;

import com.example.demo.bean.dto.OrderCreateReq;
import com.example.demo.bean.dto.OrderCreateResp;
import com.example.demo.bean.po.*;
import com.example.demo.repository.ArtistWorkRepository;
import com.example.demo.repository.OrderContentRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderItemRepository itemRepo;
    private final OrderContentRepository contentRepo;
    private final ProductRepository productRepo;
    private final ArtistWorkRepository workRepo;
    private final OrderRepository orderRepo;

    @org.springframework.transaction.annotation.Transactional
    public OrderCreateResp createOrder(OrderCreateReq req) {

        Long userId = 1L; // TODO 从登录上下文获取

        ProductPO product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (!Boolean.TRUE.equals(product.getOnSale())) {
            throw new RuntimeException("商品已下架");
        }

        // 1. Build Objects (in memory)
        OrderPO order = buildOrder(product, userId);
        OrderItemPO item = buildOrderItem(product, req);

        // Bi-directional link
        item.setOrder(order);
        order.getItems().add(item);

        List<ArtistWorkPO> works = workRepo.findByProductIdForUpdate(product.getId());

        List<OrderContentPO> contents = "BOX".equals(req.getBuyMode())
                ? buildBoxContents(product, item, works, req.getBoxCount())
                : buildSingleContents(product, item, works, req);

        // Calculate Amount
        calcAmount(order, item, contents);

        // 2. Save Order (Cascade will save items if configured, but we keep explicit
        // for safety)
        orderRepo.save(order);

        // 3. Save Item (Explicitly set orderId column as it's the owner of the physical
        // column)
        item.setOrderId(order.getId());
        itemRepo.save(item);

        // 4. Save Contents
        for (OrderContentPO content : contents) {
            content.setOrderItemId(item.getId());
        }
        contentRepo.saveAll(contents);

        return new OrderCreateResp(order.getOrderNo(), order.getPayAmount());
    }

    // ------------------ private methods ------------------

    private OrderPO buildOrder(ProductPO product, Long userId) {
        OrderPO o = new OrderPO();
        o.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        o.setUserId(userId);
        o.setArtistId(product.getArtistId());
        o.setOrderType("BOX");
        o.setOrderStatus(0);
        o.setPayStatus(0); // UNPAID
        o.setOrderTime(LocalDateTime.now());
        // o.setPayTime(LocalDateTime.now()); // Removed
        return o;
    }

    private OrderItemPO buildOrderItem(ProductPO p, OrderCreateReq req) {
        OrderItemPO i = new OrderItemPO();
        i.setProductId(p.getId());
        i.setProductTitle(p.getTitle());
        i.setCoverUrl(p.getCoverUrl());
        i.setAllowBox(p.getAllowBox());
        i.setAllowHelp(p.getAllowHelp());
        i.setNeedFollow(p.getNeedFollow());
        i.setBuyMode(req.getBuyMode());
        return i;
    }

    private List<OrderContentPO> buildBoxContents(
            ProductPO product,
            OrderItemPO item,
            List<ArtistWorkPO> works,
            int boxCount) {

        if (boxCount > works.size()) {
            throw new RuntimeException("可抽作品不足");
        }

        Collections.shuffle(works);

        List<OrderContentPO> list = new ArrayList<>();

        for (ArtistWorkPO work : works.subList(0, boxCount)) {

            ArtistWorkVersionPO sale = pickSaleVersion(work);
            list.add(buildContent(item, work, sale, "SALE", product.getOptimizedPrice()));

            ArtistWorkVersionPO gift = pickGiftVersion(work);
            list.add(buildContent(item, work, gift, "GIFT", BigDecimal.ZERO));
        }
        return list;
    }

    private List<OrderContentPO> buildSingleContents(
            ProductPO product,
            OrderItemPO item,
            List<ArtistWorkPO> works,
            OrderCreateReq req) {

        if (req.getWorkIds() == null || req.getWorkIds().isEmpty()) {
            throw new RuntimeException("请选择作品");
        }

        List<Long> selectedIds = req.getWorkIds();

        // Filter works that match the selected IDs
        List<ArtistWorkPO> selectedWorks = works.stream()
                .filter(w -> selectedIds.contains(w.getId()))
                .toList();

        if (selectedWorks.isEmpty()) {
            throw new RuntimeException("选中的作品不存在");
        }

        List<OrderContentPO> contentList = new ArrayList<>();

        for (ArtistWorkPO work : selectedWorks) {
            ArtistWorkVersionPO v = pickSaleVersion(work);
            contentList.add(buildContent(
                    item, work, v, "SALE", product.getSinglePrice()));
        }

        return contentList;
    }

    private OrderContentPO buildContent(
            OrderItemPO item,
            ArtistWorkPO work,
            ArtistWorkVersionPO v,
            String type,
            BigDecimal price) {

        OrderContentPO c = new OrderContentPO();
        c.setOrderItem(item);
        c.setArtistWorkId(work.getId());
        c.setWorkName(work.getName());
        c.setArtistWorkVersionId(v.getId());
        c.setPreviewUrl(v.getPreviewUrl());
        c.setOriginalUrl(v.getOriginalUrl());
        c.setSilhouetteUrl(v.getSilhouetteUrl());
        c.setContentType(type);
        c.setGift("GIFT".equals(type));
        c.setPrice(price);
        return c;
    }

    private ArtistWorkVersionPO pickSaleVersion(ArtistWorkPO work) {
        return work.getVersions().get(0);
    }

    private ArtistWorkVersionPO pickGiftVersion(ArtistWorkPO work) {
        return work.getVersions().get(0);
    }

    private void calcAmount(
            OrderPO order,
            OrderItemPO item,
            List<OrderContentPO> contents) {

        BigDecimal total = contents.stream()
                .filter(c -> !c.getGift())
                .map(OrderContentPO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        item.setSubtotalAmount(total);
        order.setOriginalAmount(total);
        order.setPayAmount(total);
    }
}
