package com.example.order.controller;

import com.example.order.bean.Article;
import com.example.order.repository.ArticleRepository;
import com.example.order.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/search")
    public List<Article> search(@RequestParam String keyword) {
        return articleService.searchArticles(keyword); // 执行全文搜索
    }

    @PostMapping("/save")
    public Article createArticle(@RequestBody Article article) {
        // 保存数据到 Elasticsearch
        return articleRepository.save(article);
    }
}
