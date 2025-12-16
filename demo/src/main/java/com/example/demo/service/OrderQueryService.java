package com.example.demo.service;
import com.example.demo.bean.dto.OrderContentDTO;
import com.example.demo.bean.dto.OrderDetailDTO;
import com.example.demo.bean.dto.OrderFlatDTO;
import com.example.demo.bean.dto.OrderItemDTO;
import com.example.demo.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderQueryService {


    private final OrderQueryRepository queryRepo;

    public OrderDetailDTO getOrderDetail(String orderNo) {

        List<OrderFlatDTO> rows = queryRepo.findOrderDetail(orderNo);

        if (rows.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        OrderFlatDTO first = rows.get(0);

        OrderDetailDTO detail = new OrderDetailDTO();
        detail.setOrderNo(first.getOrderNo());
        detail.setOrderStatus(first.getOrderStatus());
        detail.setPayStatus(first.getPayStatus());
        detail.setPayAmount(first.getPayAmount());
        detail.setOrderTime(first.getOrderTime());

        Map<Long, OrderItemDTO> itemMap = new LinkedHashMap<>();

        for (OrderFlatDTO r : rows) {

            OrderItemDTO item = itemMap.computeIfAbsent(
                    r.getProductId(),
                    k -> {
                        OrderItemDTO dto = new OrderItemDTO();
                        dto.setProductId(r.getProductId());
                        dto.setProductTitle(r.getProductTitle());
                        dto.setBuyMode(r.getBuyMode());
                        dto.setSubtotalAmount(r.getSubtotalAmount());
                        dto.setContents(new ArrayList<>());
                        return dto;
                    }
            );

            OrderContentDTO content = new OrderContentDTO();
            content.setArtistWorkId(r.getArtistWorkId());
            content.setWorkName(r.getWorkName());
            content.setVersionId(r.getVersionId());
            content.setPreviewUrl(r.getPreviewUrl());
            content.setContentType(r.getContentType());
            content.setPrice(r.getPrice());

            item.getContents().add(content);
        }

        detail.setItems(new ArrayList<>(itemMap.values()));
        return detail;
    }
}
