package com.example.order.controller;

import com.example.common.core.entity.vo.Result;
import com.example.order.bean.dto.WithdrawalRequest;
import com.example.order.bean.dto.WithdrawalResponse;
import com.example.order.service.WechatWithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * 微信提现控制器
 */
@RestController
@RequestMapping("/order/wechat/withdrawal")
@Tag(name = "微信提现接口")
public class WechatWithdrawalController {

    @Autowired
    private WechatWithdrawalService wechatWithdrawalService;

    @PostMapping("/apply")
    @Operation(summary = "申请提现", description = "用户申请提现到微信零钱")
    public Result<WithdrawalResponse> applyWithdrawal(
            @RequestBody WithdrawalRequest request,
            Authentication authentication) {

        // 从JWT中获取用户ID
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");

        WithdrawalResponse response = wechatWithdrawalService.initiateWithdrawal(userId, request);

        Result<WithdrawalResponse> result = new Result<>();
        result.setCode(Result.SUCCESSFUL_CODE);
        result.setMesg(Result.SUCCESSFUL_MESG);
        result.setData(response);
        result.setTime(java.time.Instant.now());
        return result;
    }

    @GetMapping("/query/{batchId}")
    @Operation(summary = "查询提现状态", description = "根据批次ID查询提现状态")
    public Result<WithdrawalResponse> queryWithdrawal(@PathVariable String batchId) {
        WithdrawalResponse response = wechatWithdrawalService.queryWithdrawalStatus(batchId);

        Result<WithdrawalResponse> result = new Result<>();
        result.setCode(Result.SUCCESSFUL_CODE);
        result.setMesg(Result.SUCCESSFUL_MESG);
        result.setData(response);
        result.setTime(java.time.Instant.now());
        return result;
    }
}
