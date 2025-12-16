package com.example.demo.service;

import com.example.demo.bean.dto.ProductDTO;
import com.example.demo.bean.dto.ProductDetailDTO;
import com.example.demo.bean.dto.ProductListDTO;
import com.example.demo.bean.form.ProductQueryForm;
import com.example.demo.bean.form.ProductSaveForm;
import com.example.demo.bean.form.ProductUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductDTO> listAll();

    Long saveProduct(ProductSaveForm form);


    Page<ProductListDTO> list(ProductQueryForm form, Pageable pageable);


    ProductDetailDTO detail(Long productId);

    void updateProduct(ProductUpdateForm form);
}
