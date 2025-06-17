package com.example.order.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private static final String PRODUCT_STOCK_KEY = "product:stock:";

    // 获取商品库存
    public int getProductStock(int productId) {
        Integer stock = redisTemplate.opsForValue().get(PRODUCT_STOCK_KEY + productId);
        return stock == null ? 0 : stock;
    }

    // 更新商品库存
    public void updateProductStock(int productId, int quantity) {
        redisTemplate.opsForValue().set(PRODUCT_STOCK_KEY + productId, quantity);
    }
}
