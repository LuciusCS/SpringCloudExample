#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import json
import logging
import time
from datetime import datetime
from flask import jsonify

from aliyunsdkdysmsapi.request.v20170525 import SendSmsRequest
from aliyunsdkcore.client import AcsClient

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# 阿里云短信配置
ACCESS_KEY_ID = ""
ACCESS_KEY_SECRET = ""
SIGN_NAME = ""
TEMPLATE_CODE = ""  # 复电短信

# 目标手机号（可以是多个手机号，逗号分隔）
PHONE_NUMBERS = ""

# 用于去重的缓存（简单实现，生产环境建议使用 Redis）
# 格式: {alert_fingerprint: last_sent_timestamp}
sent_alerts_cache = {}
CACHE_EXPIRE_SECONDS = 300  # 5分钟内相同告警不重复发送


def create_aliyun_client():
    """创建阿里云短信客户端"""
    return AcsClient(ACCESS_KEY_ID, ACCESS_KEY_SECRET, "cn-hangzhou")


def format_time(iso_time_str):
    """格式化 ISO 8601 时间为友好格式"""
    try:
        if not iso_time_str:
            return "未知时间"
        # 处理 Z 结尾的 UTC 时间
        time_str = iso_time_str.replace('Z', '+00:00')
        dt = datetime.fromisoformat(time_str)
        return dt.strftime('%Y-%m-%d %H:%M:%S')
    except Exception as e:
        logger.warning(f"时间格式化失败: {e}, 原始时间: {iso_time_str}")
        return iso_time_str


def get_alert_fingerprint(alert):
    """生成告警指纹用于去重"""
    labels = alert.get("labels", {})
    alertname = labels.get("alertname", "unknown")
    instance = labels.get("instance", "unknown")
    status = alert.get("status", "unknown")
    return f"{alertname}:{instance}:{status}"


def should_send_alert(fingerprint):
    """检查是否应该发送告警（去重逻辑）"""
    current_time = time.time()
    
    # 清理过期缓存
    expired_keys = [k for k, v in sent_alerts_cache.items() 
                    if current_time - v > CACHE_EXPIRE_SECONDS]
    for key in expired_keys:
        del sent_alerts_cache[key]
    
    # 检查是否在缓存中
    if fingerprint in sent_alerts_cache:
        last_sent = sent_alerts_cache[fingerprint]
        if current_time - last_sent < CACHE_EXPIRE_SECONDS:
            logger.info(f"告警 {fingerprint} 在 {CACHE_EXPIRE_SECONDS} 秒内已发送，跳过")
            return False
    
    # 更新缓存
    sent_alerts_cache[fingerprint] = current_time
    return True


def send_sms(phone_numbers, message):
    """发送短信"""
    try:
        client = create_aliyun_client()
        
        # 创建短信请求
        request = SendSmsRequest.SendSmsRequest()
        request.set_TemplateCode(TEMPLATE_CODE)
        request.set_SignName(SIGN_NAME)
        request.set_PhoneNumbers(phone_numbers)
        
        # 设置短信模板参数
        request.set_TemplateParam(json.dumps(message))
        
        # 发送短信请求
        response = client.do_action_with_exception(request)
        response_dict = json.loads(response.decode('utf-8'))
        
        if response_dict.get('Code') == 'OK':
            logger.info(f"短信发送成功: {phone_numbers}, 内容: {message}")
            return True
        else:
            logger.error(f"短信发送失败: {response_dict}")
            return False
            
    except Exception as e:
        logger.error(f"短信发送异常: {e}", exc_info=True)
        return False


def format_alert_message(alert):
    """格式化告警信息为短信模板参数"""
    labels = alert.get("labels", {})
    annotations = alert.get("annotations", {})
    status = alert.get("status", "firing")
    
    # 提取信息
    alertname = labels.get("alertname", "未知告警")
    instance = annotations.get("instance") or labels.get("instance", "未知实例")
    severity = labels.get("severity", "warning")
    description = annotations.get("description", "无描述")
    starts_at = alert.get("startsAt", "")
    
    # 格式化时间
    formatted_time = format_time(starts_at)
    
    # 根据状态调整事件描述
    if status == "resolved":
        event = f"【已恢复】{description}"
    else:
        event = description
    
    # 返回短信模板参数
    return {
        "name": alertname[:20],  # 限制长度，避免超出短信模板限制
        "address": instance[:30],
        "date": formatted_time,
        "event": event[:50],  # 限制长度
    }


def handle_alert(data):
    """处理 Prometheus 告警 Webhook"""
    try:
        if not data:
            logger.warning("收到空数据")
            return jsonify({"status": "error", "message": "No data"}), 400
        
        alerts = data.get("alerts", [])
        logger.info(f"收到 {len(alerts)} 条告警")
        
        sent_count = 0
        skipped_count = 0
        
        for alert in alerts:
            # 生成告警指纹
            fingerprint = get_alert_fingerprint(alert)
            
            # 检查是否应该发送（去重）
            if not should_send_alert(fingerprint):
                skipped_count += 1
                continue
            
            # 格式化告警数据
            alert_data = format_alert_message(alert)
            
            # 发送短信
            if send_sms(PHONE_NUMBERS, alert_data):
                sent_count += 1
            
            # 添加延迟，避免短时间内发送过多短信
            if sent_count > 0 and sent_count % 3 == 0:
                time.sleep(1)
        
        logger.info(f"短信发送完成: 发送 {sent_count} 条, 跳过 {skipped_count} 条")
        
        return jsonify({
            "status": "ok",
            "sent": sent_count,
            "skipped": skipped_count
        }), 200
        
    except Exception as e:
        logger.error(f"处理告警失败: {e}", exc_info=True)
        return jsonify({"status": "error", "message": str(e)}), 500
