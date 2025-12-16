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

    private String previewUrl;
    private String originalUrl;
    private String silhouetteUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_work_id")
    private ArtistWorkPO artistWork;
}
