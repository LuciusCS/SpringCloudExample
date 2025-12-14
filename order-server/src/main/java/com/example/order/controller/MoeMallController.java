package com.example.order.controller;

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
public class MoeMallController {

    // Mock Data for Home Page (Waterfall)
    @GetMapping("/products/home")
    public Map<String, Object> getHomeProducts() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        list.add(createProduct("1", "鲷鱼烧章鱼烧", "5.88", "https://i.postimg.cc/prXq0d0V/prod1.jpg", "糕糕扭蛋铺", "2.5k"));
        list.add(createProduct("2", "三张披萨 x 福利直 go", "0.52", "https://i.postimg.cc/zX8qjV1M/prod2.jpg", "沦为雪", "1.2w"));
        list.add(createProduct("3", "小熊娃娃", "2.00", "https://i.postimg.cc/J0y2WzqL/prod3.jpg", "白雪小店", "629"));
        list.add(createProduct("4", "帕尼尼 x 像素椰子姐妹头", "6.60", "https://i.postimg.cc/L5d2XqKj/prod4.jpg", "帕尼尼直购", "2.4w"));
        list.add(createProduct("5", "可爱萌妹头像", "9.99", "https://i.postimg.cc/Hx8qjV1N/prod5.jpg", "萌萌哒", "1.5k"));

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", list);
        return result;
    }

    // Mock Data for User Info (Mine Page)
    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        
        data.put("username", "小枝");
        data.put("userId", "330147646");
        data.put("avatar", "https://i.postimg.cc/Kj8qjV1P/avatar.jpg"); // Placeholder
        data.put("balance", "0");
        data.put("level", "V1");

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", data);
        return result;
    }

    // Mock Data for My House
    @GetMapping("/house/items")
    public Map<String, Object> getHouseItems() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> categories = new ArrayList<>();

        // Category 1
        Map<String, Object> cat1 = new HashMap<>();
        cat1.put("name", "热恋期扭蛋");
        cat1.put("expanded", true);
        List<Map<String, Object>> items1 = new ArrayList<>();
        items1.add(createHouseItem("101", "小楠熊2号机", "https://i.postimg.cc/prXq0d0V/prod1.jpg", false));
        items1.add(createHouseItem("102", "小黑熊", "https://i.postimg.cc/zX8qjV1M/prod2.jpg", false));
        items1.add(createHouseItem("103", "限定款", "https://i.postimg.cc/J0y2WzqL/prod3.jpg", true));
        cat1.put("items", items1);
        categories.add(cat1);

        // Category 2
        Map<String, Object> cat2 = new HashMap<>();
        cat2.put("name", "粉色星球的扭蛋铺");
        cat2.put("expanded", true);
        List<Map<String, Object>> items2 = new ArrayList<>();
        items2.add(createHouseItem("201", "粉色星球冬日限定", "https://i.postimg.cc/L5d2XqKj/prod4.jpg", false));
        cat2.put("items", items2);
        categories.add(cat2);

        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", categories);
        return result;
    }

    // Mock Data for Product Detail
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductDetail(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // Return generic data for any ID for now
        data.put("id", id);
        data.put("title", "赠图：少女的祈祷");
        data.put("imageUrl", "https://i.postimg.cc/prXq0d0V/prod1.jpg"); // Use one of the images
        data.put("acquireTime", "2025/04/30 00:37");
        data.put("type", "赠图");
        
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", data);
        return result;
    }

    private Map<String, Object> createProduct(String id, String title, String price, String img, String shop, String likes) {
        Map<String, Object> p = new HashMap<>();
        p.put("id", id);
        p.put("title", title);
        p.put("price", price);
        p.put("image", img);
        p.put("shopName", shop);
        p.put("likes", likes);
        return p;
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
