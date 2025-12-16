package com.example.demo.service;

import com.example.demo.bean.dto.ProductDTO;
import com.example.demo.bean.form.ProductSaveForm;

import java.util.List;

public interface ProductService {

    List<ProductDTO> listAll();

    Long saveProduct(ProductSaveForm form);
}
