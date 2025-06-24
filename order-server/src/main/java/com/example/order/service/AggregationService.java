//package com.example.order.service;
//
//import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
//import com.example.order.bean.Article;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.client.elc.Aggregation;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class AggregationService {
//
////    @Autowired
////    private ElasticsearchRestTemplate elasticsearchTemplate;
////
////    public void aggregationQuery() {
////        // 创建聚合查询（按分类聚合统计数量）
////        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("category_aggregation").field("category.keyword");
////
////        // 执行聚合查询
////        AggregationResults result = elasticsearchRestTemplate.aggregate(QueryBuilders.matchAllQuery(), Article.class, aggregationBuilder);
////
////        // 获取聚合结果
////        System.out.println(result);
////    }
//}
