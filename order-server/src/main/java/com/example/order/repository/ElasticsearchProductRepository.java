package com.example.order.repository;

import com.example.order.bean.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchProductRepository extends ElasticsearchRepository<Product, String> {

}
