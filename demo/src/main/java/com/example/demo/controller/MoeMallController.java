package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moe")
@lombok.RequiredArgsConstructor
public class MoeMallController {

    private final com.example.demo.service.PurchasedContentService purchasedContentService;

    // Mock Data for My House -> Real Data
    @GetMapping("/house/items")
    public Map<String, Object> getHouseItems() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> categories = new ArrayList<>();

        Long userId = 1L; // Mock User
        List<com.example.demo.bean.dto.PurchasedContentDTO> purchasedList = purchasedContentService
                .listPurchasedContents(userId);

        // Group by logic:
        // We will make a single "Category" called "我的收藏" for now, or group by nothing
        // strictly?
        // Frontend expects: List<Category> -> List<Subgroup> -> List<Item>
        // Let's map:
        // Category = "我的收藏" (Single)
        // Subgroup = Product Title
        // Item = Work Version

        if (!purchasedList.isEmpty()) {
            Map<String, Object> mainCat = new HashMap<>();
            mainCat.put("name", "我的收藏");
            mainCat.put("expanded", true);

            List<Map<String, Object>> subgroupList = new ArrayList<>();

            for (com.example.demo.bean.dto.PurchasedContentDTO p : purchasedList) {
                Map<String, Object> sub = new HashMap<>();
                sub.put("name", p.getProductTitle());

                List<Map<String, Object>> items = new ArrayList<>();
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



    private Map<String, Object> createHouseItem(String id, String name, String img, boolean isLimited) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("name", name);
        item.put("image", img);
        item.put("isLimited", isLimited);
        return item;
    }
}
