package com.example.demo.controller;

import com.example.demo.bean.dto.PurchasedContentDTO;
import com.example.demo.service.PurchasedContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/content")
@RequiredArgsConstructor
public class PurchasedContentController {

    private final PurchasedContentService service;

    @GetMapping("/purchased")
    public List<PurchasedContentDTO> list() {
        Long userId = 1L; // Mock user ID
        return service.listPurchasedContents(userId);
    }
}
