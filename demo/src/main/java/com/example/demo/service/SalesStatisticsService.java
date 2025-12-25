package com.example.demo.service;

import com.example.demo.bean.dto.EarningsStatsDTO;
import com.example.demo.bean.dto.SalesRecordDTO;
import com.example.demo.bean.po.*;
import com.example.demo.repository.ProductSalesRecordRepository;
import com.example.demo.repository.WorkSalesRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesStatisticsService {

    private final ProductSalesRecordRepository productSalesRepo;
    private final WorkSalesRecordRepository workSalesRepo;

    /**
     * 记录订单相关的销售统计数据
     * 应在订单支付成功后调用
     *
     * @param order 已完成支付的订单
     */
    @Transactional
    public void recordSales(OrderPO order) {
        log.info("Starting to record sales statistics for OrderNo: {}", order.getOrderNo());

        if (order.getItems() == null || order.getItems().isEmpty()) {
            log.warn("Order has no items, skipping statistics. OrderNo: {}", order.getOrderNo());
            return;
        }

        List<ProductSalesRecordPO> productRecords = new ArrayList<>();
        List<WorkSalesRecordPO> workRecords = new ArrayList<>();

        for (OrderItemPO item : order.getItems()) {
            // 1. 创建商品销售记录
            ProductSalesRecordPO productRecord = new ProductSalesRecordPO();
            productRecord.setProductId(item.getProductId());
            productRecord.setProductName(item.getProductTitle());
            // 优先使用订单上的画师ID（通常跟商品一致），也可从ProductPO查
            productRecord.setArtistId(order.getArtistId());
            productRecord.setOrderId(order.getId());
            productRecord.setOrderNo(order.getOrderNo());
            productRecord.setPayTime(order.getPayTime());
            productRecord.setAmount(item.getSubtotalAmount());
            productRecord.setQuantity(item.getBuyQuantity());
            productRecords.add(productRecord);

            // 2. 创建作品销售记录 (如果有具体的 OrderContent)
            if (item.getContents() != null) {
                for (OrderContentPO content : item.getContents()) {
                    // 只记录有收益的内容，也可以记录 GIFT 但金额为0
                    WorkSalesRecordPO workRecord = new WorkSalesRecordPO();
                    workRecord.setArtistWorkId(content.getArtistWorkId());
                    workRecord.setWorkName(content.getWorkName());
                    workRecord.setProductId(item.getProductId());
                    workRecord.setArtistId(order.getArtistId());
                    workRecord.setOrderId(order.getId());
                    workRecord.setOrderNo(order.getOrderNo());
                    workRecord.setPayTime(order.getPayTime());
                    workRecord.setAmount(content.getPrice());
                    workRecords.add(workRecord);
                }
            }
        }

        if (!productRecords.isEmpty()) {
            productSalesRepo.saveAll(productRecords);
        }
        if (!workRecords.isEmpty()) {
            workSalesRepo.saveAll(workRecords);
        }

        log.info("Recorded {} product sales and {} work sales for OrderNo: {}",
                productRecords.size(), workRecords.size(), order.getOrderNo());
    }

    /**
     * 查询商品收益统计及列表
     */
    public EarningsStatsDTO getProductEarnings(Long productId, Pageable pageable) {
        EarningsStatsDTO stats = new EarningsStatsDTO();

        // 1. 统计总金额
        BigDecimal total = productSalesRepo.sumAmountByProductId(productId);
        stats.setTotalEarnings(total != null ? total : BigDecimal.ZERO);

        // 2. 统计总销量
        Integer count = productSalesRepo.sumQuantityByProductId(productId);
        stats.setTotalSalesCount(count != null ? count : 0);

        // 3. 查询列表
        Page<ProductSalesRecordPO> page = productSalesRepo.findByProductIdOrderByPayTimeDesc(productId, pageable);
        List<SalesRecordDTO> records = page.getContent().stream().map(po -> {
            SalesRecordDTO dto = new SalesRecordDTO();
            dto.setId(po.getId());
            dto.setOrderNo(po.getOrderNo());
            dto.setPayTime(po.getPayTime());
            dto.setAmount(po.getAmount());
            dto.setQuantity(po.getQuantity());
            dto.setName(po.getProductName());
            return dto;
        }).collect(Collectors.toList());

        stats.setRecords(records);
        return stats;
    }

    /**
     * 查询作品收益统计及列表
     */
    public EarningsStatsDTO getWorkEarnings(Long workId, Pageable pageable) {
        EarningsStatsDTO stats = new EarningsStatsDTO();

        // 1. 统计总金额
        BigDecimal total = workSalesRepo.sumAmountByArtistWorkId(workId);
        stats.setTotalEarnings(total != null ? total : BigDecimal.ZERO);

        // 作品通常没有"销量"概念（因为可能包含在不同商品中），这里可以不填或填记录数
        // 如果需要次数，可以用 count(*)

        // 2. 查询列表
        Page<WorkSalesRecordPO> page = workSalesRepo.findByArtistWorkIdOrderByPayTimeDesc(workId, pageable);
        List<SalesRecordDTO> records = page.getContent().stream().map(po -> {
            SalesRecordDTO dto = new SalesRecordDTO();
            dto.setId(po.getId());
            dto.setOrderNo(po.getOrderNo());
            dto.setPayTime(po.getPayTime());
            dto.setAmount(po.getAmount());
            // 作品记录没有数量概念，通常是 1
            dto.setQuantity(1);
            dto.setName(po.getWorkName());
            return dto;
        }).collect(Collectors.toList());

        // 列表大小即为销售次数
        stats.setTotalSalesCount((int) page.getTotalElements());
        stats.setRecords(records);

        return stats;
    }
}
