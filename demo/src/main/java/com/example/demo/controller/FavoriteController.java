package com.example.demo.controller;

import com.example.demo.bean.dto.FavoriteToggleReq;
import com.example.demo.bean.dto.ProductListDTO;
import com.example.demo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle")
    public Boolean toggleFavorite(@RequestBody FavoriteToggleReq req) {
        return favoriteService.toggleFavorite(req);
    }

    @GetMapping("/check")
    public Boolean checkFavorite(@RequestParam Long targetId, @RequestParam Integer type) {
        return favoriteService.checkFavorite(targetId, type);
    }

    @GetMapping("/list")
    public List<ProductListDTO> listFavorites(@RequestParam(required = false) Integer type) {
        return favoriteService.listFavorites(type);
    }
}
