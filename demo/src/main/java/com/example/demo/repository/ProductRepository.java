package com.example.demo.repository;

import com.example.demo.bean.po.ProductPO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductPO, Long>, JpaSpecificationExecutor<ProductPO> {

        @EntityGraph(attributePaths = {
                        "works"
        })
        List<ProductPO> findAll();

        @EntityGraph(attributePaths = {
                        "works"
        })
        Optional<ProductPO> findWithAllById(Long id);
}