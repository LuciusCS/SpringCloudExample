package com.example.order.repository;

import com.example.order.bean.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleRepository  extends ElasticsearchRepository<Article, String> {

    /**
     * findByTitleContaining 是一个由 Spring Data 自动实现的方法，能根据 部分标题 执行 模糊匹配。
     * @param title
     * @return
     */
    List<Article> findByTitleContaining(String title); // 用于全文搜索
}