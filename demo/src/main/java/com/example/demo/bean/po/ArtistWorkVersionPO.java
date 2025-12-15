package com.example.demo.bean.po;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.ArrayList;

@Entity
@Table(name = "artist_work_version")
@Data
public class ArtistWorkVersionPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preview_url", length = 512)
    private String previewUrl;
    @Column(name = "original_url", length = 512)
    private String originalUrl;
    @Column(name = "silhouette_url", length = 512)
    private String silhouetteUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_work_id")
    private ArtistWorkPO artistWork;
}
