package com.example.order.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现请求DTO
 */
@Data
@Schema(description = "提现请求")
public class WithdrawalRequest {

    @Schema(description = "用户OpenID")
    private String openid;

    @Schema(description = "真实姓名（必须与微信账号一致）")
    private String realName;

    @Schema(description = "提现金额")
    private BigDecimal amount;

    @Schema(description = "备注")
    private String remark;
}
