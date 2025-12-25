package com.example.demo.controller;

import com.example.demo.bean.dto.EarningsStatsDTO;
import com.example.demo.service.SalesStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class SalesStatisticsController {

    private final SalesStatisticsService salesStatisticsService;

    /**
     * 查询指定商品的总收益和销售记录列表
     * 
     * @param productId 商品ID
     * @param pageable  分页参数，默认按 payingTime 倒序
     */
    @GetMapping("/product/{productId}")
    public EarningsStatsDTO getProductEarnings(
            @PathVariable Long productId,
            @PageableDefault(size = 20, sort = "payTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return salesStatisticsService.getProductEarnings(productId, pageable);
    }

    /**
     * 查询指定作品（OrderContent维度）的总收益和销售记录列表
     * 
     * @param workId   作品ID (ArtistWorkID)
     * @param pageable 分页参数
     */
    @GetMapping("/work/{workId}")
    public EarningsStatsDTO getWorkEarnings(
            @PathVariable Long workId,
            @PageableDefault(size = 20, sort = "payTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return salesStatisticsService.getWorkEarnings(workId, pageable);
    }
}
