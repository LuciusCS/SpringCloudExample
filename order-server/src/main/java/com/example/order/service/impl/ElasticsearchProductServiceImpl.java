package com.example.order.service.impl;


import com.example.order.bean.po.elasticsearch.Product;
import com.example.order.repository.ElasticsearchProductRepository;
import com.example.order.service.ElasticsearchProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.TermQueryBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ElasticsearchProductServiceImpl implements ElasticsearchProductService {
    @Autowired
    private ElasticsearchProductRepository productRepository;
    @Autowired
    private  ElasticsearchOperations elasticsearchOperations;

    @Override
    public Product save(Product product) {
       return   productRepository.save(product);
    }


    @Override
    public void update(Product product) {

        productRepository.save(product);
    }


    //根据 id 查询
    @Override
    public Product findById(String id) {
        // Optional<T> findById(ID id)，返回值是Optional ,如无值时抛出 NoSuchElementException
        return  productRepository.findById(id).get();

    }

    @Override
    public List<Product>  findAll() {
        Iterable<Product> products = productRepository.findAll();

       return StreamSupport.stream(products.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {

        productRepository.deleteById(id);
    }


    //批量新增
    @Override
    public void saveAll(List<Product> productList) {

         productRepository.saveAll(productList);
    }

    //分页查询
    @Override
    public Page<Product>  findByPageable(Pageable pageable) {

        Page<Product> productPage = productRepository.findAll(pageable);

        return  productPage;
    }

//    /**
//     * term 查询
//     * search(termQueryBuilder) 调用搜索方法，参数查询构建器对象
//     */
//
//    public void termQuery() {
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
//        Iterable<Product> products = productRepository.search(termQueryBuilder);
//        for (Product product : products) {
//            System.out.println(product);
//        }
//    }
//
//    /**
//     * term 查询加分页
//     */
//
//    public void termQueryByPage() {
//        int currentPage = 0;
//        int pageSize = 5;
//        //设置查询分页
//        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
//        Iterable<Product> products = productDao.search(termQueryBuilder, pageRequest);
//        for (Product product : products) {
//            System.out.println(product);
//        }
//    }


}
