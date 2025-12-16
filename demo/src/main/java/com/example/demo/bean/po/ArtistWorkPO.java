package com.example.demo.bean.po;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist_work")
@Data
public class ArtistWorkPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer workType;
    private String tags;
    private BigDecimal transparency;
    private Integer saleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductPO product;

    @OneToMany(
            mappedBy = "artistWork",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ArtistWorkVersionPO> versions = new ArrayList<>();
}