package com.example.order.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提现响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "提现响应")
public class WithdrawalResponse {

    @Schema(description = "批次ID")
    private String batchId;

    @Schema(description = "商户批次号")
    private String outBatchNo;

    @Schema(description = "状态: PENDING, PROCESSING, SUCCESS, FAILED")
    private String status;

    @Schema(description = "消息")
    private String message;
}
