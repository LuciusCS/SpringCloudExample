package com.example.order.service;

import com.example.order.bean.po.elasticsearch.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElasticsearchProductService {
    /**
     * 新增产品
     * @param product
     * @return
     */
    Product save(Product product);

    /**
     * 更新产品信息
     * @param product
     */
    void update(Product product);

    /**
     * 根据产品ID 查询产品
     * @param id
     * @return
     */
    Product findById(String id);

    /**
     * 查询所有的产品列表
     * @return
     */
    List<Product> findAll();

    /**
     * 根据id 删除产品
     * @param id
     */
    void delete(String id);

    /**
     * 批量添加数据
     * @param productList
     */
    void saveAll(List<Product> productList);

    /**
     * 分页查询数据
     * @param pageable
     * @return
     */
    Page<Product> findByPageable(Pageable pageable);
}
