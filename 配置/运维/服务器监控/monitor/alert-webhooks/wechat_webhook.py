#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import time
import logging
from datetime import datetime
from flask import jsonify

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# ======= 微信服务号配置 =======
APP_ID = ""
APP_SECRET = ""
TEMPLATE_ID = ""

# 接收告警的用户 openId 列表
USER_LIST = [
    ""
]

# 缓存 access_token
ACCESS_TOKEN = None
EXPIRE_AT = 0


def format_time(iso_time_str):
    """格式化 ISO 8601 时间为友好格式"""
    try:
        if not iso_time_str:
            return "未知时间"
        # 处理 Z 结尾的 UTC 时间
        time_str = iso_time_str.replace('Z', '+00:00')
        dt = datetime.fromisoformat(time_str)
        # 微信模板要求的格式
        return dt.strftime('%Y年%m月%d日 %H:%M:%S')
    except Exception as e:
        logger.warning(f"时间格式化失败: {e}, 原始时间: {iso_time_str}")
        return "未知时间"


def get_severity_text(severity):
    """将严重程度转换为中文"""
    severity_map = {
        "critical": "严重",
        "warning": "警告",
        "info": "提示",
    }
    return severity_map.get(severity.lower(), severity)


def get_access_token():
    """获取微信 access_token（自动缓存）"""
    global ACCESS_TOKEN, EXPIRE_AT
    
    # 如果 token 未过期，直接返回
    if ACCESS_TOKEN and time.time() < EXPIRE_AT:
        return ACCESS_TOKEN
    
    try:
        url = f"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APP_ID}&secret={APP_SECRET}"
        resp = requests.get(url, timeout=10).json()
        
        if "access_token" not in resp:
            logger.error(f"获取 access_token 失败: {resp}")
            return None
        
        ACCESS_TOKEN = resp["access_token"]
        EXPIRE_AT = time.time() + resp["expires_in"] - 200  # 提前 200 秒过期
        
        logger.info("access_token 更新成功")
        return ACCESS_TOKEN
        
    except Exception as e:
        logger.error(f"获取 access_token 异常: {e}", exc_info=True)
        return None


def send_wechat_template(open_id, alert_data):
    """发送微信模板消息"""
    try:
        access_token = get_access_token()
        if not access_token:
            logger.error("无法获取 access_token，跳过发送")
            return False
        
        url = f"https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={access_token}"
        
        # 构建模板消息数据
        data = {
            "touser": open_id,
            "template_id": TEMPLATE_ID,
            "data": {
                "time3": {"value": alert_data["time"]},
                "thing16": {"value": alert_data["severity"][:20]},  # 限制长度
                "thing2": {"value": alert_data["alertname"][:20]},
                "thing46": {"value": alert_data["instance"][:20]},
                "phrase20": {"value": alert_data["status"][:5]},
            }
        }
        
        resp = requests.post(url, json=data, timeout=10).json()
        
        if resp.get("errcode") == 0:
            logger.info(f"微信消息发送成功: {open_id}")
            return True
        else:
            logger.error(f"微信消息发送失败: {resp}")
            return False
            
    except Exception as e:
        logger.error(f"微信消息发送异常: {e}", exc_info=True)
        return False


def format_alert_data(alert):
    """格式化告警数据"""
    labels = alert.get("labels", {})
    annotations = alert.get("annotations", {})
    status = alert.get("status", "firing")
    
    # 提取信息
    alertname = labels.get("alertname", "未知告警")
    severity = labels.get("severity", "warning")
    instance = annotations.get("instance") or labels.get("instance", "未知实例")
    description = annotations.get("description", "无描述")
    starts_at = alert.get("startsAt", "")
    
    # 格式化时间
    formatted_time = format_time(starts_at)
    
    # 获取严重程度中文
    severity_text = get_severity_text(severity)
    
    # 状态文本
    status_text = "已恢复" if status == "resolved" else "告警中"
    
    return {
        "alertname": alertname,
        "severity": severity_text,
        "instance": instance,
        "description": description,
        "time": formatted_time,
        "status": status_text,
    }


def handle_alert(data):
    """处理 Prometheus 告警 Webhook"""
    try:
        if not data:
            logger.warning("收到空数据")
            return jsonify({"status": "error", "message": "No data"}), 400
        
        alerts = data.get("alerts", [])
        logger.info(f"收到 {len(alerts)} 条告警")
        
        success_count = 0
        failed_count = 0
        
        for alert in alerts:
            try:
                # 格式化告警数据
                alert_data = format_alert_data(alert)
                
                # 推送到每位用户
                for uid in USER_LIST:
                    if send_wechat_template(uid, alert_data):
                        success_count += 1
                    else:
                        failed_count += 1
                    
                    # 避免发送过快
                    if len(USER_LIST) > 1:
                        time.sleep(0.3)
                        
            except Exception as e:
                logger.error(f"处理单条告警失败: {e}", exc_info=True)
                failed_count += 1
        
        logger.info(f"微信消息发送完成: 成功 {success_count} 条, 失败 {failed_count} 条")
        
        return jsonify({
            "status": "ok",
            "success": success_count,
            "failed": failed_count
        }), 200
        
    except Exception as e:
        logger.error(f"处理告警失败: {e}", exc_info=True)
        return jsonify({"status": "error", "message": str(e)}), 500
