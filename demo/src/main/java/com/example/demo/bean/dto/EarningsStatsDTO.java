package com.example.demo.bean.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EarningsStatsDTO {

    /** 总收益 */
    private BigDecimal totalEarnings;

    /** 总销量 (仅商品有意义，作品可能无此概念或为1) */
    private Integer totalSalesCount;

    /** 交易列表 */
    private List<SalesRecordDTO> records;
}
