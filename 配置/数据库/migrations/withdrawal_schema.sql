-- 微信提现功能数据库表
-- 用于记录所有提现请求和状态

USE spring_cloud_db;

-- 创建用户余额表（如果不存在）
CREATE TABLE IF NOT EXISTS user_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
    frozen_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '冻结余额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) COMMENT='用户余额表';

-- 创建提现记录表
CREATE TABLE withdrawal_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    openid VARCHAR(64) NOT NULL COMMENT '微信OpenID',
    real_name VARCHAR(100) NOT NULL COMMENT '真实姓名',
    amount DECIMAL(10,2) NOT NULL COMMENT '提现金额',
    batch_id VARCHAR(64) UNIQUE COMMENT '微信批次ID',
    out_batch_no VARCHAR(64) UNIQUE COMMENT '商户批次号',
    out_detail_no VARCHAR(64) UNIQUE COMMENT '商户明细号',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING, PROCESSING, SUCCESS, FAILED',
    remark VARCHAR(200) COMMENT '备注',
    fail_reason VARCHAR(500) COMMENT '失败原因',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_batch_id (batch_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) COMMENT='提现记录表';

-- 创建余额变动记录表
CREATE TABLE balance_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '变动金额（正数为增加，负数为减少）',
    balance_after DECIMAL(10,2) NOT NULL COMMENT '变动后余额',
    type VARCHAR(20) NOT NULL COMMENT '类型: RECHARGE, WITHDRAWAL, REFUND, COMMISSION',
    related_id BIGINT COMMENT '关联ID（如提现记录ID）',
    remark VARCHAR(200) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
) COMMENT='余额变动记录表';
