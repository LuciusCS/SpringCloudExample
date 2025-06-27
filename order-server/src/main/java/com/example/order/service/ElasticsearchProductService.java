package com.example.order.service;

import com.example.order.bean.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElasticsearchProductService {
    Product save(Product product);

    void update(Product product);

    //根据 id 查询
    Product findById(String id);

    List<Product> findAll();

    void delete(String id);

    //批量新增
    void saveAll(List<Product> productList);

    //分页查询
    Page<Product> findByPageable(Pageable pageable);
}
