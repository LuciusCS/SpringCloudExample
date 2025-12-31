package com.example.demo.service;

import com.example.demo.bean.dto.FavoriteToggleReq;
import com.example.demo.bean.dto.ProductListDTO;
import com.example.demo.bean.po.Favorite;
import com.example.demo.bean.po.ProductPO;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public boolean toggleFavorite(FavoriteToggleReq req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Favorite existing = favoriteRepository.findByUserIdAndTargetIdAndType(userId, req.getTargetId(), req.getType());
        if (existing != null) {
            favoriteRepository.delete(existing);
            return false; // Removed
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setTargetId(req.getTargetId());
            favorite.setType(req.getType());
            favoriteRepository.save(favorite);
            return true; // Added
        }
    }

    public boolean checkFavorite(Long targetId, Integer type) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null)
            return false;
        Favorite existing = favoriteRepository.findByUserIdAndTargetIdAndType(userId, targetId, type);
        return existing != null;
    }

    public List<ProductListDTO> listFavorites(Integer type) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<Favorite> favorites;
        if (type != null) {
            favorites = favoriteRepository.findAllByUserIdAndType(userId, type);
        } else {
            favorites = favoriteRepository.findAllByUserId(userId);
        }

        if (favorites.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> productIds = favorites.stream()
                .map(Favorite::getTargetId)
                .collect(Collectors.toSet());

        List<ProductPO> products = productRepository.findAllById(productIds);

        // Map products to DTOs
        return products.stream().map(p -> {
            ProductListDTO dto = new ProductListDTO();
            BeanUtils.copyProperties(p, dto);
            // Re-map some likely differences depending on ProductListDTO definition
            // Assuming ProductListDTO roughly matches ProductPO fields for list view
            return dto;
        }).collect(Collectors.toList());
    }
}
