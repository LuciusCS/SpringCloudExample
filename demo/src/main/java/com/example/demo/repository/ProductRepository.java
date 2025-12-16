package com.example.demo.repository;

import com.example.demo.bean.po.ProductPO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductPO, Long> {

    @EntityGraph(attributePaths = {
            "works",
            "works.versions"
    })
    List<ProductPO> findAll();
}