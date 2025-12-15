package com.example.demo.bean.mapper;


import com.example.demo.bean.dto.ProductDTO;
import com.example.demo.bean.form.ArtistWorkForm;
import com.example.demo.bean.form.ArtistWorkVersionForm;
import com.example.demo.bean.form.ProductSaveForm;
import com.example.demo.bean.po.ArtistWorkPO;
import com.example.demo.bean.po.ArtistWorkVersionPO;
import com.example.demo.bean.po.ProductPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productPOToProductDTO(ProductPO productPO);
    ProductPO productDTOToProductPO(ProductDTO productDTO);

    @Mapping(target = "works", source = "works")
    ProductPO productSaveFormToProductPO(ProductSaveForm productSaveForm);

    // 映射 ArtistWorkForm 列表到 ArtistWorkPO 列表
    List<ArtistWorkPO> artistWorkFormListToArtistWorkPOList(List<ArtistWorkForm> works);


    // 映射 ArtistWorkForm 到 ArtistWorkPO
    @Mapping(target = "versions", source = "versions")
    ArtistWorkPO artistWorkFormToArtistWorkPO(ArtistWorkForm artistWorkForm);

    // 映射 ArtistWorkVersionForm 列表到 ArtistWorkVersionPO 列表
    List<ArtistWorkVersionPO> artistWorkVersionFormListToArtistWorkVersionPOList(List<ArtistWorkVersionForm> versions);


}
