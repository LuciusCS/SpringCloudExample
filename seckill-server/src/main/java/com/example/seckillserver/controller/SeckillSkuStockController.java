package com.example.seckillserver.controller;


import com.example.seckillserver.api.dto.SeckillSkuDTO;
import com.example.seckillserver.ratelimit.RedisRateLimitImpl;
import com.example.seckillserver.standard.redis.RedisRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.example.seckillserver.api.dto.SeckillDTO;

import com.example.seckillserver.page.PageOut;
import com.example.seckillserver.page.PageReq;
import com.example.seckillserver.result.RestOut;

import com.example.seckillserver.service.SeckillSkuStockServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;



@RestController
@RequestMapping("/api/seckill/sku/")
@Tag(name =  "商品库存")
public class SeckillSkuStockController {
    @Resource
    SeckillSkuStockServiceImpl seckillSkuStockService;


    @Resource(name = "redisRateLimitImpl")
    RedisRateLimitImpl rateLimitService;



    /**
     * 获取所有的秒杀商品列表
     *
     * @param pageReq 当前页 ，从1 开始,和 页的元素个数
     * @return
     */
    @PostMapping("/list/v1")
    @Operation(summary = "全部秒杀商品")
    RestOut<PageOut<SeckillSkuDTO>> findAll(@RequestBody PageReq pageReq) {
        PageOut<SeckillSkuDTO> page = seckillSkuStockService.findAll(pageReq);
        RestOut<PageOut<SeckillSkuDTO>> r = RestOut.success(page);
        return r;

    }

    /**
     * 查询商品信息
     *
     * @param dto 商品id
     * @return 商品 skuDTO
     */
    @PostMapping("/detail/v1")
    @Operation(summary =  "秒杀商品详情")
    RestOut<SeckillSkuDTO> skuDetail(@RequestBody SeckillDTO dto) {
        Long skuId = dto.getSeckillSkuId();

        SeckillSkuDTO skuDTO = seckillSkuStockService.detail(skuId);

        if (null != skuDTO) {

            return RestOut.success(skuDTO).setRespMsg("查找成功");
        }


        return RestOut.error("未找到指定秒杀商品");
    }

    /**
     * 删除商品信息
     *
     * @param dto 商品id
     * @return 商品 skuDTO
     */
    @PostMapping("/delete/v1")
    @Operation(summary =  "删除商品信息")
    RestOut<SeckillSkuDTO> deleteSku(@RequestBody SeckillDTO dto) {
        Long skuId = dto.getSeckillSkuId();

        seckillSkuStockService.delete(skuId);


        return RestOut.error("删除商品信息 ok");
    }


    /**
     * 设置秒杀库存
     *
     * @param dto 商品与库存
     * @return 商品 skuDTO
     */
    @PutMapping("/stock/v1")
    @Operation(summary = "设置秒杀库存")
    RestOut<SeckillSkuDTO> setStock(@RequestBody SeckillDTO dto) {
        Long skuId = dto.getSeckillSkuId();
        int stock = dto.getNewStockNum();

        SeckillSkuDTO skuDTO = seckillSkuStockService.setNewStock(skuId, stock);

        if (null != skuDTO) {
            return RestOut.success(skuDTO).setRespMsg("设置秒杀库存成功");
        }
        return RestOut.error("未找到指定秒杀商品");
    }


    /**
     * 增加秒杀的商品
     *
     * @param stockCount 库存
     * @param title      标题
     * @param price      商品原价格
     * @param costPrice  价格
     * @return
     */
    @PostMapping("/add/v1")
    @Operation(summary = "增加秒杀商品")
    @Parameters({
            @Parameter(name = "title", description = "商品名称",  required = true, example = "秒杀商品-1"),
            @Parameter(name = "stockCount", description = "秒杀数量",  required = true,  example = "10000"),
            @Parameter(name = "price", description = "原始价格",  required = true,example = "1000"),
            @Parameter(name = "costPrice", description = "秒杀价格", required = true,example = "1000")
    })
    RestOut<SeckillSkuDTO> addSeckill(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "stockCount", required = true) int stockCount,
            @RequestParam(value = "price", required = true) float price,
            @RequestParam(value = "costPrice", required = true) float costPrice) {
        SeckillSkuDTO dto = seckillSkuStockService.addSeckillSku(title, stockCount, price, costPrice);
        return RestOut.success(dto).setRespMsg("增加秒杀的商品成功");

    }


    /**
     * 暴露商品秒杀

     {
     "exposedKey": "4b70903f6e1aa87788d3ea962f8b2f0e",
     "newStockNum": 10000,
     "seckillSkuId": 1157197244718385152,
     "seckillToken": "0f8459cbae1748c7b14e4cea3d991000",
     "userId": 37
     }

     * @param dto 商品id
     * @return 商品 skuDTO
     */
    @PostMapping("/expose/v1")
    @Operation(summary = "暴露商品秒杀")
    RestOut<SeckillSkuDTO> expose(@RequestBody SeckillDTO dto) {
        Long skuId = dto.getSeckillSkuId();

        SeckillSkuDTO skuDTO = seckillSkuStockService.detail(skuId);

        if (null == skuDTO) {
            return RestOut.error("未找到指定秒杀商品");
        }

        //初始化秒杀的限流器
        rateLimitService.initLimitKey(
                "seckill",
                String.valueOf(skuId),
                10000000,//总数 SeckillConstants.MAX_ENTER,
                1000// 100/s  SeckillConstants.PER_SECKOND_ENTER
        );


        //暴露秒杀
        skuDTO = seckillSkuStockService.exposeSeckillSku(skuId);

        return RestOut.success(skuDTO).setRespMsg("秒杀开启成功");


    }

}
