package com.example.seckillserver.dao.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.io.Serializable;

/**
 * 秒杀商品PO 的库存
 */

@Entity
@Table(name = "SECKILL_SEGMENT_STOCK")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeckillSegmentStockPO implements Serializable
{
    //商品ID
    @Id
//    @GenericGenerator(
//            name = "snowflakeIdGenerator",
//            strategy = "com.crazymaker.springcloud.standard.hibernate.CommonSnowflakeIdGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEG_STOCK_ID", unique = true, nullable = false, length = 8)
    private Long id;

    //商品id，和订单是一对多的关系
    @Column(name = "SKU_ID")
    private Long skuId;

    //分段的编号，从0开始
    @Column(name = "SEG_INDEX")
    private Integer segmentIndex;


    //剩余库存数量
    @Column(name = "STOCK_COUNT")
    private long stockCount;
    //原始库存数量
    @Column(name = "RAW_STOCK")
    private long rawStockCount;
}
