package com.example.demo.service.impl;

import com.example.demo.bean.dto.*;
import com.example.demo.bean.form.*;
import com.example.demo.bean.mapper.ProductMapper;
import com.example.demo.bean.po.ArtistWorkPO;
import com.example.demo.bean.po.ArtistWorkVersionPO;
import com.example.demo.bean.po.ProductPO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.specification.ProductSpecification;
import com.example.demo.service.MinioService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productDao;

    private final MinioService minioService;

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

    @Override
    public Page<ProductListDTO> list(ProductQueryForm form, Pageable pageable) {
        Specification<ProductPO> spec = ProductSpecification.build(form);

        return productDao.findAll(spec, pageable)
                .map(this::toListDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailDTO detail(Long productId) {
        ProductPO product = productDao.findWithAllById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        return toDetailDTO(product);
        // return null;
    }

    @Transactional
    @Override
    public void updateProduct(ProductUpdateForm form) {
        ProductPO product = productDao.findWithAllById(form.getId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 1️⃣ 更新商品字段
        product.setArtistId(form.getArtistId());
        product.setCoverUrl(form.getCoverUrl());
        product.setTitle(form.getTitle());
        product.setTags(form.getTags());
        product.setThemeColor(form.getThemeColor());

        // 2️⃣ 更新作品列表
        List<ArtistWorkPO> newWorks = new ArrayList<>();

        if (form.getWorks() != null) {
            for (ArtistWorkUpdateForm workForm : form.getWorks()) {

                ArtistWorkPO work;
                if (workForm.getId() != null) {
                    // 更新现有作品
                    work = product.getWorks().stream()
                            .filter(w -> w.getId().equals(workForm.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("作品不存在"));
                } else {
                    // 新增作品
                    work = new ArtistWorkPO();
                    work.setProduct(product);
                }

                // 更新字段
                work.setName(workForm.getName());
                work.setWorkType(workForm.getWorkType());
                work.setTags(workForm.getTags());
                work.setTransparency(workForm.getTransparency());
                work.setSaleType(workForm.getSaleType());

                // 3️⃣ 更新作品版本
                List<ArtistWorkVersionPO> newVersions = new ArrayList<>();
                if (workForm.getVersions() != null) {
                    for (ArtistWorkVersionUpdateForm vForm : workForm.getVersions()) {
                        ArtistWorkVersionPO version;
                        if (vForm.getId() != null) {
                            version = work.getVersions().stream()
                                    .filter(v -> v.getId().equals(vForm.getId()))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("版本不存在"));
                        } else {
                            version = new ArtistWorkVersionPO();
                            version.setArtistWork(work);
                        }

                        version.setPreviewUrl(vForm.getPreviewUrl());
                        version.setOriginalUrl(vForm.getOriginalUrl());
                        version.setSilhouetteUrl(vForm.getSilhouetteUrl());
                        newVersions.add(version);
                    }
                }
                // 替换版本列表（orphanRemoval=true 会自动删除不在列表中的旧版本）
                work.setVersions(newVersions);
                newWorks.add(work);
            }
        }

        // 替换作品列表（orphanRemoval=true 会自动删除不在列表中的旧作品）
        product.setWorks(newWorks);

        // 4️⃣ 保存（事务自动更新）
        productDao.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productDao.deleteById(id);
    }

    private ProductDTO toDTO(ProductPO po) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(po, dto);
        // Only transforming coverUrl if needed here, but usually list is what matters
        // most for cover
        dto.setCoverUrl(po.getCoverUrl());

        dto.setWorks(
                po.getWorks().stream().map(work -> {
                    ArtistWorkDTO w = new ArtistWorkDTO();
                    BeanUtils.copyProperties(work, w);

                    w.setVersions(
                            work.getVersions().stream().map(v -> {
                                ArtistWorkVersionDTO vd = new ArtistWorkVersionDTO();
                                BeanUtils.copyProperties(v, vd);
                                return vd;
                            }).toList());
                    return w;
                }).toList());
        return dto;
    }

    private ProductPO buildProductPO(ProductSaveForm form) {

        // 1. 商品
        ProductPO product = ProductMapper.INSTANCE.productSaveFormToProductPO(form);
        // 手动为每个 ArtistWorkPO 设置 product 属性
        for (ArtistWorkPO work : product.getWorks()) {
            work.setProduct(product);// 设置每个 work 的 product 为 productPO

            for (ArtistWorkVersionPO artistWorkVersionPO : work.getVersions()) {
                artistWorkVersionPO.setArtistWork(work);
            }
        }
        ;

        return product;
    }

    private ProductListDTO toListDTO(ProductPO po) {
        ProductListDTO dto = new ProductListDTO();
        dto.setId(po.getId());
        dto.setArtistId(po.getArtistId());
        // Use MinioService to get public URL
//        dto.setCoverUrl(minioService.getPublicUrl(po.getCoverUrl()));
        dto.setCoverUrl(po.getCoverUrl());
        dto.setTitle(po.getTitle());
        dto.setTags(po.getTags());
        dto.setThemeColor(po.getThemeColor());
        // dto.setCreateTime(po.getCreateTime());
        return dto;
    }

    private ProductDetailDTO toDetailDTO(ProductPO po) {

        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(po.getId());
        dto.setArtistId(po.getArtistId());
        // Use MinioService to get public URL
        dto.setCoverUrl(po.getCoverUrl());
        dto.setTitle(po.getTitle());
        dto.setTags(po.getTags());
        dto.setThemeColor(po.getThemeColor());

        if (po.getWorks() != null) {
            dto.setWorks(
                    po.getWorks().stream().map(work -> {

                        ArtistWorkDTO workDTO = new ArtistWorkDTO();
                        workDTO.setId(work.getId());
                        workDTO.setName(work.getName());
                        workDTO.setWorkType(work.getWorkType());
                        workDTO.setTags(work.getTags());
                        workDTO.setTransparency(work.getTransparency());
                        workDTO.setSaleType(work.getSaleType());

                        if (work.getVersions() != null) {
                            workDTO.setVersions(
                                    work.getVersions().stream().map(v -> {
                                        ArtistWorkVersionDTO vDTO = new ArtistWorkVersionDTO();
                                        vDTO.setId(v.getId());
                                        vDTO.setPreviewUrl(v.getPreviewUrl());
                                        vDTO.setOriginalUrl(v.getOriginalUrl());
                                        vDTO.setSilhouetteUrl(v.getSilhouetteUrl());
                                        return vDTO;
                                    }).toList());
                        }

                        return workDTO;
                    }).toList());
        }

        return dto;
    }

}
