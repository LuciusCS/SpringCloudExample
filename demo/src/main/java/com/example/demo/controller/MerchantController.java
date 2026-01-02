package com.example.demo.controller;

import com.example.demo.bean.dto.MerchantAuditReviewReq;
import com.example.demo.bean.dto.MerchantAuditSubmitReq;
import com.example.demo.bean.po.MerchantAuditPO;
import com.example.demo.repository.MerchantAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moe/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantAuditRepository auditRepository;
    private final com.example.demo.repository.StoreRepository storeRepository;

    // --- Audit Endpoints ---

    @PostMapping("/audit/submit")
    public Map<String, Object> submitAudit(@RequestBody MerchantAuditSubmitReq req) {
        System.out.println("Submit Audit Req: " + req);
        MerchantAuditPO po = new MerchantAuditPO();
        po.setUserId(req.getUserId());
        po.setMerchantName(req.getMerchantName());
        po.setProofImages(req.getProofImages());
        po.setQqContact(req.getQqContact());
        po.setStatus(0); // Pending

        auditRepository.save(po);

        return Map.of("code", 0, "msg", "提交成功");
    }

    @GetMapping("/audit/list")
    public Map<String, Object> getAuditList(@RequestParam(required = false) Integer status) {
        List<MerchantAuditPO> list;
        if (status != null) {
            list = auditRepository.findByStatus(status);
        } else {
            list = auditRepository.findAll();
        }
        return Map.of("code", 0, "msg", "success", "data", list);
    }

    @PostMapping("/audit/review")
    public Map<String, Object> reviewAudit(@RequestBody MerchantAuditReviewReq req) {
        MerchantAuditPO po = auditRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Audit record not found"));

        if (Boolean.TRUE.equals(req.getPass())) {
            po.setStatus(1); // Approved

            System.out.println("Review Audit Passed. PO UserId: " + po.getUserId());

            // Check if store exists
            if (po.getUserId() != null) {
                var existingStore = storeRepository.findByUserId(po.getUserId());
                if (existingStore.isEmpty()) {
                    System.out.println("Creating new store for user: " + po.getUserId());
                    com.example.demo.bean.po.StorePO store = new com.example.demo.bean.po.StorePO();
                    store.setUserId(po.getUserId());
                    store.setName(po.getMerchantName());
                    store.setContact(po.getQqContact());
                    storeRepository.save(store);
                } else {
                    System.out.println("Store already exists for user: " + po.getUserId());
                }
            } else {
                // Log warning or handle error: Audit record has no userId, cannot create store
                System.err.println("Warning: Audit record " + po.getId() + " has no userId. Cannot create store.");
            }

        } else {
            po.setStatus(2); // Rejected
            po.setRejectReason(req.getRejectReason());
        }
        auditRepository.save(po);

        return Map.of("code", 0, "msg", "审核完成");
    }

    // --- Store Info Management ---

    @GetMapping("/store/info")
    public Map<String, Object> getStoreInfo(@RequestParam Long userId) {
        var storeOpt = storeRepository.findByUserId(userId);
        if (storeOpt.isEmpty()) {
            return Map.of("code", 0, "msg", "success", "data", null);
        }
        return Map.of("code", 0, "msg", "success", "data", storeOpt.get());
    }

    @PostMapping("/store/update")
    public Map<String, Object> updateStoreInfo(@RequestBody com.example.demo.bean.dto.StoreUpdateReq req) {
        System.out.println("Received Store Update Request: " + req);
        if (req.getUserId() == null) {
            System.out.println("Error: UserId is required");
            return Map.of("code", 1, "msg", "UserId is required");
        }
        var storeOpt = storeRepository.findByUserId(req.getUserId());
        com.example.demo.bean.po.StorePO store;

        if (storeOpt.isEmpty()) {
            // Implicitly create store if it doesn't exist?
            // Users might want to set up store info before audit?
            // For now, let's create it to be user friendly
            store = new com.example.demo.bean.po.StorePO();
            store.setUserId(req.getUserId());
        } else {
            store = storeOpt.get();
        }

        if (req.getName() != null && !req.getName().isBlank())
            store.setName(req.getName());
        if (req.getDescription() != null)
            store.setDescription(req.getDescription());
        if (req.getLogo() != null)
            store.setLogo(req.getLogo());

        storeRepository.save(store);

        return Map.of("code", 0, "msg", "更新成功");
    }

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
