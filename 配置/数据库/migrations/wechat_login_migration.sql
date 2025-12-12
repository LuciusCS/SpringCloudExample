-- 微信登录功能数据库迁移脚本
-- 为 user 表添加微信相关字段

USE spring_cloud_db;

-- 添加微信OpenID字段（唯一索引）
ALTER TABLE user ADD COLUMN wechat_openid VARCHAR(64) UNIQUE COMMENT '微信OpenID';

-- 添加微信UnionID字段
ALTER TABLE user ADD COLUMN wechat_unionid VARCHAR(64) COMMENT '微信UnionID';

-- 添加昵称字段
ALTER TABLE user ADD COLUMN nickname VARCHAR(100) COMMENT '昵称';

-- 添加头像URL字段
ALTER TABLE user ADD COLUMN avatar_url VARCHAR(500) COMMENT '头像URL';

-- 创建索引以提高查询性能
CREATE INDEX idx_wechat_openid ON user(wechat_openid);
CREATE INDEX idx_wechat_unionid ON user(wechat_unionid);
