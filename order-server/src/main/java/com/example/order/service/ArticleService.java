package com.example.order.service;

import com.example.order.bean.Article;
import com.example.order.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> searchArticles(String keyword) {
        return articleRepository.findByTitleContaining(keyword);  // 根据标题执行全文搜索
    }
}
