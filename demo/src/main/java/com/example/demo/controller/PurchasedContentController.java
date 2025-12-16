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

        Long userId = LoginContext.getUserId(); // 从登录态取
        return service.listPurchasedContents(userId);
    }
}
