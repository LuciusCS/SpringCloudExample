package com.example.demo.controller;

import com.example.demo.bean.dto.OrderCreateReq;
import com.example.demo.bean.dto.OrderCreateResp;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final com.example.demo.service.PurchasedContentService purchasedContentService;

    @PostMapping("/create")
    public OrderCreateResp create(@RequestBody OrderCreateReq req) {
        return orderService.createOrder(req);
    }

    // Moved from MoeMallController
    @GetMapping("/house/items")
    public java.util.Map<String, Object> getHouseItems() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        java.util.List<java.util.Map<String, Object>> categories = new java.util.ArrayList<>();

        Long userId = 1L; // Mock User
        java.util.List<com.example.demo.bean.dto.PurchasedContentDTO> purchasedList = purchasedContentService
                .listPurchasedContents(userId);

        if (!purchasedList.isEmpty()) {
            java.util.Map<String, Object> mainCat = new java.util.HashMap<>();
            mainCat.put("name", "我的收藏");
            mainCat.put("expanded", true);

            java.util.List<java.util.Map<String, Object>> subgroupList = new java.util.ArrayList<>();

            for (com.example.demo.bean.dto.PurchasedContentDTO p : purchasedList) {
                java.util.Map<String, Object> sub = new java.util.HashMap<>();
                sub.put("name", p.getProductTitle());

                java.util.List<java.util.Map<String, Object>> items = new java.util.ArrayList<>();
                if (p.getWorks() != null) {
                    for (com.example.demo.bean.dto.PurchasedWorkDTO w : p.getWorks()) {
                        if (w.getVersions() != null) {
                            for (com.example.demo.bean.dto.PurchasedVersionDTO v : w.getVersions()) {
                                items.add(createHouseItem(
                                        String.valueOf(v.getVersionId()),
                                        w.getWorkName(),
                                        v.getPreviewUrl(),
                                        false // No limit info in DTO yet
                                ));
                            }
                        }
                    }
                }
                sub.put("items", items);
                subgroupList.add(sub);
            }
            mainCat.put("subgroups", subgroupList);
            categories.add(mainCat);
        }

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", categories);
        return result;
    }

    private java.util.Map<String, Object> createHouseItem(String id, String name, String img, boolean isLimited) {
        java.util.Map<String, Object> item = new java.util.HashMap<>();
        item.put("id", id);
        item.put("name", name);
        item.put("image", img);
        item.put("isLimited", isLimited);
        return item;
    }
}
