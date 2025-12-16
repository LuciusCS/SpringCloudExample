package com.example.demo.service.impl;

import com.example.demo.bean.dto.ArtistWorkDTO;
import com.example.demo.bean.dto.ArtistWorkVersionDTO;
import com.example.demo.bean.dto.ProductDTO;
import com.example.demo.bean.form.ProductSaveForm;
import com.example.demo.bean.po.ArtistWorkPO;
import com.example.demo.bean.po.ArtistWorkVersionPO;
import com.example.demo.bean.po.ProductPO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productDao;

    @Override
    public List<ProductDTO> listAll() {
        return List.of();
    }

    @Transactional
    public Long saveProduct(ProductSaveForm form) {
        ProductPO product = buildProductPO(form);
        productDao.save(product); // 一次 save，级联全部保存
        return product.getId();
    }

    private ProductDTO toDTO(ProductPO po) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(po, dto);

        dto.setWorks(
                po.getWorks().stream().map(work -> {
                    ArtistWorkDTO w = new ArtistWorkDTO();
                    BeanUtils.copyProperties(work, w);

                    w.setVersions(
                            work.getVersions().stream().map(v -> {
                                ArtistWorkVersionDTO vd = new ArtistWorkVersionDTO();
                                BeanUtils.copyProperties(v, vd);
                                return vd;
                            }).toList()
                    );
                    return w;
                }).toList()
        );
        return dto;
    }

    private ProductPO buildProductPO(ProductSaveForm form) {

        // 1. 商品
        ProductPO product = new ProductPO();
        product.setArtistId(form.getArtistId());
        product.setCoverUrl(form.getCoverUrl());
        product.setTitle(form.getTitle());
        product.setTags(form.getTags());
        product.setThemeColor(form.getThemeColor());

        // 2. 作品列表
        if (form.getWorks() != null) {
            List<ArtistWorkPO> works = form.getWorks().stream().map(workForm -> {

                ArtistWorkPO work = new ArtistWorkPO();
                work.setName(workForm.getName());
                work.setWorkType(workForm.getWorkType());
                work.setTags(workForm.getTags());
                work.setTransparency(workForm.getTransparency());
                work.setSaleType(workForm.getSaleType());

                // ⭐ 关键：建立双向关系
                work.setProduct(product);

                // 3. 作品版本
                if (workForm.getVersions() != null) {
                    List<ArtistWorkVersionPO> versions =
                            workForm.getVersions().stream().map(versionForm -> {

                                ArtistWorkVersionPO version = new ArtistWorkVersionPO();
                                version.setPreviewUrl(versionForm.getPreviewUrl());
                                version.setOriginalUrl(versionForm.getOriginalUrl());
                                version.setSilhouetteUrl(versionForm.getSilhouetteUrl());

                                // ⭐ 关键：建立双向关系
                                version.setArtistWork(work);
                                return version;

                            }).toList();

                    work.setVersions(versions);
                }

                return work;

            }).toList();

            product.setWorks(works);
        }

        return product;
    }
}
