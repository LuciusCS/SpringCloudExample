package com.example.order.controller;


import com.example.order.bean.po.elasticsearch.Product;
import com.example.order.service.impl.ElasticsearchProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "ElasticsearchProductController",description = "产品信息管理")
@RestController
@RequestMapping("/order/product")
public class ElasticsearchProductController {

    @Autowired
    ElasticsearchProductServiceImpl elasticsearchProductService;

    @PostMapping("/save")
    @Operation(summary = "添加产品")
    public Product save(@RequestBody Product product) {

       return elasticsearchProductService.save(product);
    }
    //POSTMAN, GET http://localhost:9200/product/_doc/2

    //修改
//    public void update() {
//        Product product = new Product();
//        product.setId(2L);
//        product.setTitle("小米 2 手机");
//        product.setCategory("手机");
//        product.setPrice(9999.0);
//        product.setImages("http://www.baidu/xm.jpg");
//        productDao.save(product);
//    }
    //POSTMAN, GET http://localhost:9200/product/_doc/2


    @GetMapping("/findById")
    public Product findById(@RequestParam String id) {
       return elasticsearchProductService.findById(id);

    }

    @GetMapping("/findAll")
    public List<Product> findAll() {
        List<Product> products = elasticsearchProductService.findAll();

        return  products;
    }
//
//    //删除
//    public void delete() {
//        Product product = new Product();
//        product.setId(2L);
//        productDao.delete(product);
//    }
//    //POSTMAN, GET http://localhost:9200/product/_doc/2
//
//    //批量新增
//    public void saveAll() {
//        List<Product> productList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Product product = new Product();
//            product.setId(Long.valueOf(i));
//            product.setTitle("[" + i + "]小米手机");
//            product.setCategory("手机");
//            product.setPrice(1999.0 + i);
//            product.setImages("http://www.baidu/xm.jpg");
//            productList.add(product);
//        }
//        productDao.saveAll(productList);
//    }
//
//    //分页查询
//    public void findByPageable() {
//        //设置排序(排序方式，正序还是倒序，排序的 id)
//        Sort sort = Sort.by(Sort.Direction.DESC, "id");
//        int currentPage = 0;//当前页，第一页从 0 开始， 1 表示第二页
//        int pageSize = 5;//每页显示多少条
//        //设置查询分页
//        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
//        //分页查询
//        Page<Product> productPage = productDao.findAll(pageRequest);
//        for (Product Product : productPage.getContent()) {
//            System.out.println(Product);
//        }
//    }

}
