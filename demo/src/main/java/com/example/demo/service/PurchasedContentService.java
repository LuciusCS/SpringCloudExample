package com.example.demo.service;
import com.example.demo.bean.dto.PurchasedContentDTO;
import com.example.demo.bean.dto.PurchasedContentFlatDTO;
import com.example.demo.bean.dto.PurchasedVersionDTO;
import com.example.demo.bean.dto.PurchasedWorkDTO;
import com.example.demo.repository.PurchasedContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchasedContentService {

    private final PurchasedContentRepository repository;

    public List<PurchasedContentDTO> listPurchasedContents(Long userId) {

        List<PurchasedContentFlatDTO> rows =
                repository.findPurchasedContents(userId);

        Map<Long, PurchasedContentDTO> productMap = new LinkedHashMap<>();

        for (PurchasedContentFlatDTO r : rows) {

            // product
            PurchasedContentDTO product =
                    productMap.computeIfAbsent(
                            r.getProductId(),
                            k -> {
                                PurchasedContentDTO dto = new PurchasedContentDTO();
                                dto.setProductId(r.getProductId());
                                dto.setProductTitle(r.getProductTitle());
                                dto.setWorks(new ArrayList<>());
                                return dto;
                            }
                    );

            // work
            Map<Long, PurchasedWorkDTO> workMap =
                    product.getWorks()
                            .stream()
                            .collect(Collectors.toMap(
                                    PurchasedWorkDTO::getArtistWorkId,
                                    w -> w,
                                    (a, b) -> a,
                                    LinkedHashMap::new
                            ));

            PurchasedWorkDTO work =
                    workMap.computeIfAbsent(
                            r.getArtistWorkId(),
                            k -> {
                                PurchasedWorkDTO w = new PurchasedWorkDTO();
                                w.setArtistWorkId(r.getArtistWorkId());
                                w.setWorkName(r.getWorkName());
                                w.setVersions(new ArrayList<>());
                                product.getWorks().add(w);
                                return w;
                            }
                    );

            // version
            PurchasedVersionDTO version = new PurchasedVersionDTO();
            version.setVersionId(r.getVersionId());
            version.setPreviewUrl(r.getPreviewUrl());
            version.setOriginalUrl(r.getOriginalUrl());
            version.setBuyTime(r.getBuyTime());

            work.getVersions().add(version);
        }

        return new ArrayList<>(productMap.values());
    }
}
