package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moe/merchant")
public class MerchantController {

    // 1. Merchant Stats (Wallet, Views, Fans)
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("balance", "127.67");
        data.put("views", "50933");
        data.put("fans", "1.1w");
        
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", data);
        return result;
    }

    // 2. Merchant Products (Selling, Drafts, etc.)
    @GetMapping("/products")
    public Map<String, Object> getProducts() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        list.add(createProduct("7k粉福利购", "199.46", "/prizes/prize1.jpg", "2025/11/30 21:56上架"));
        list.add(createProduct("不白x二颗颗二 摸鱼个人机", "4951.92", "/prizes/prize2.jpg", "2025/11/30 00:00上架"));
        list.add(createProduct("风杏x二颗直购", "2542.41", "/prizes/prize3.jpg", "2025/11/16 06:40上架"));

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", list);
        return result;
    }

    // 3. Merchant Config (Tags, Colors)
    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // Tags
        List<String> tags = new ArrayList<>();
        tags.add("日漫");
        tags.add("厚涂");
        tags.add("情侣");
        tags.add("男生");
        tags.add("女生");
        data.put("tags", tags);

        // Theme Colors
        List<String> colors = new ArrayList<>();
        colors.add("#E1BEE7"); // Purple
        colors.add("#F8BBD0"); // Pink
        colors.add("#B3E5FC"); // Blue
        colors.add("#FFF9C4"); // Yellow
        colors.add("#C8E6C9"); // Green
        data.put("colors", colors);

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", data);
        return result;
    }

    private Map<String, Object> createProduct(String title, String revenue, String img, String status) {
        Map<String, Object> p = new HashMap<>();
        p.put("title", title);
        p.put("revenue", revenue);
        p.put("image", img);
        p.put("status", status);
        return p;
    }
}
