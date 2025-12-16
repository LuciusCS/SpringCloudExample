package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class ProductPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;
    private  Integer type;

    ///用于表示下架
    private Boolean onSale;


    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ArtistWorkPO> works = new ArrayList<>();
}
