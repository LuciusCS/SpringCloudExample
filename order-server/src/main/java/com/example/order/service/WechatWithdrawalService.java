package com.example.order.service;

import com.example.order.bean.UserBalance;
import com.example.order.bean.WithdrawalRecord;
import com.example.order.bean.dto.WithdrawalRequest;
import com.example.order.bean.dto.WithdrawalResponse;
import com.example.order.config.WechatPayProperties;
import com.example.order.repository.UserBalanceRepository;
import com.example.order.repository.WithdrawalRecordRepository;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.transferbatch.TransferBatchService;
import com.wechat.pay.java.service.transferbatch.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * 微信提现服务
 */
@Service
@Slf4j
public class WechatWithdrawalService {

    @Autowired
    private WechatPayProperties wechatPayProperties;

    @Autowired
    private TransferBatchService transferBatchService;

    @Autowired
    private RSAAutoCertificateConfig wechatPaySdkConfig;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private WithdrawalRecordRepository withdrawalRecordRepository;

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.30");
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("20000.00");

    /**
     * 发起提现申请
     *
     * @param userId  用户ID
     * @param request 提现请求
     * @return 提现响应
     */
    @Transactional
    public WithdrawalResponse initiateWithdrawal(Long userId, WithdrawalRequest request) {
        log.info("用户 {} 发起提现申请, 金额: {}", userId, request.getAmount());

        // 1. 参数验证
        validateWithdrawalRequest(request);

        // 2. 检查用户余额（使用悲观锁）
        UserBalance userBalance = userBalanceRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("用户余额不存在"));

        if (userBalance.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 3. 创建提现记录
        WithdrawalRecord record = new WithdrawalRecord();
        record.setUserId(userId);
        record.setOpenid(request.getOpenid());
        record.setRealName(request.getRealName());
        record.setAmount(request.getAmount());
        record.setOutBatchNo("BATCH" + System.currentTimeMillis() + userId);
        record.setOutDetailNo("DETAIL" + System.currentTimeMillis() + userId);
        record.setStatus("PENDING");
        record.setRemark(request.getRemark() != null ? request.getRemark() : "用户提现");

        withdrawalRecordRepository.save(record);

        // 4. 扣减用户余额
        userBalance.setBalance(userBalance.getBalance().subtract(request.getAmount()));
        userBalanceRepository.save(userBalance);

        try {
            // 5. 调用微信转账API
            InitiateBatchTransferRequest transferRequest = buildTransferRequest(record);
            InitiateBatchTransferResponse transferResponse = transferBatchService
                    .initiateBatchTransfer(transferRequest);

            // 6. 更新提现记录
            record.setBatchId(transferResponse.getBatchId());
            record.setStatus("PROCESSING");
            withdrawalRecordRepository.save(record);

            log.info("提现申请成功, batchId: {}", transferResponse.getBatchId());

            return WithdrawalResponse.builder()
                    .batchId(transferResponse.getBatchId())
                    .outBatchNo(record.getOutBatchNo())
                    .status("PROCESSING")
                    .message("提现申请已提交，处理中")
                    .build();

        } catch (Exception e) {
            log.error("提现失败", e);

            // 回滚余额
            userBalance.setBalance(userBalance.getBalance().add(request.getAmount()));
            userBalanceRepository.save(userBalance);

            // 更新提现记录为失败
            record.setStatus("FAILED");
            record.setFailReason(e.getMessage());
            withdrawalRecordRepository.save(record);

            throw new RuntimeException("提现失败: " + e.getMessage());
        }
    }

    /**
     * 查询提现状态
     *
     * @param batchId 批次ID
     * @return 提现响应
     */
    public WithdrawalResponse queryWithdrawalStatus(String batchId) {
        WithdrawalRecord record = withdrawalRecordRepository.findByBatchId(batchId)
                .orElseThrow(() -> new RuntimeException("提现记录不存在"));

        try {
            // 调用微信API查询批次状态
            GetTransferBatchByNoRequest queryRequest = new GetTransferBatchByNoRequest();
            queryRequest.setNeedQueryDetail(false);
            queryRequest.setBatchId(batchId);

            TransferBatchEntity queryResponse = transferBatchService
                    .getTransferBatchByNo(queryRequest);

            // 更新本地记录状态
            String wechatStatus = queryResponse.getTransferBatch().getBatchStatus();
            if ("FINISHED".equals(wechatStatus)) {
                record.setStatus("SUCCESS");
            } else if ("CLOSED".equals(wechatStatus)) {
                record.setStatus("FAILED");
                record.setFailReason("批次已关闭");
            }
            withdrawalRecordRepository.save(record);

            return WithdrawalResponse.builder()
                    .batchId(batchId)
                    .outBatchNo(record.getOutBatchNo())
                    .status(record.getStatus())
                    .message("查询成功")
                    .build();

        } catch (Exception e) {
            log.error("查询提现状态失败", e);
            return WithdrawalResponse.builder()
                    .batchId(batchId)
                    .outBatchNo(record.getOutBatchNo())
                    .status(record.getStatus())
                    .message("查询失败: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 构建转账请求
     */
    private InitiateBatchTransferRequest buildTransferRequest(WithdrawalRecord record) {
        InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
        request.setAppid(wechatPayProperties.getAppid());
        request.setOutBatchNo(record.getOutBatchNo());
        request.setBatchName("用户提现");
        request.setBatchRemark(record.getRemark());
        request.setTotalAmount(record.getAmount().multiply(new BigDecimal("100")).longValue());
        request.setTotalNum(1);

        // 构建转账明细
        TransferDetailInput detail = new TransferDetailInput();
        detail.setOutDetailNo(record.getOutDetailNo());
        detail.setTransferAmount(record.getAmount().multiply(new BigDecimal("100")).longValue());
        detail.setTransferRemark(record.getRemark());
        detail.setOpenid(record.getOpenid());

        // 加密真实姓名
        String encryptedName = encryptRealName(record.getRealName());
        detail.setUserName(encryptedName);

        request.setTransferDetailList(Collections.singletonList(detail));

        return request;
    }

    /**
     * 加密真实姓名（使用微信支付平台公钥）
     */
    private String encryptRealName(String realName) {
        return wechatPaySdkConfig.createEncryptor().encrypt(realName);
    }

    /**
     * 验证提现请求参数
     */
    private void validateWithdrawalRequest(WithdrawalRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(MIN_AMOUNT) < 0) {
            throw new RuntimeException("提现金额不能小于 " + MIN_AMOUNT + " 元");
        }

        if (request.getAmount().compareTo(MAX_AMOUNT) > 0) {
            throw new RuntimeException("提现金额不能大于 " + MAX_AMOUNT + " 元");
        }

        if (request.getOpenid() == null || request.getOpenid().trim().isEmpty()) {
            throw new RuntimeException("OpenID不能为空");
        }

        if (request.getRealName() == null || request.getRealName().trim().isEmpty()) {
            throw new RuntimeException("真实姓名不能为空");
        }
    }
}
