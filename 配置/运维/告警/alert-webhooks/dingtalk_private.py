#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
钉钉私聊消息发送模块
使用企业内部应用发送工作通知到个人
"""

import requests
import json
import time
import logging
from datetime import datetime

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# ======= 钉钉企业内部应用配置 =======
# 需要在钉钉开发者后台创建企业内部应用
APP_KEY = "your_app_key"  # 替换为您的 AppKey
APP_SECRET = "your_app_secret"  # 替换为您的 AppSecret

# 接收告警的用户 UserID 列表
# UserID 可以通过手机号查询获得
USER_IDS = [
    # "manager001",  # 取消注释并填写 UserID
]

# 缓存 access_token
ACCESS_TOKEN = None
EXPIRE_AT = 0


def get_access_token():
    """获取钉钉 access_token"""
    global ACCESS_TOKEN, EXPIRE_AT
    
    # 如果 token 未过期，直接返回
    if ACCESS_TOKEN and time.time() < EXPIRE_AT:
        return ACCESS_TOKEN
    
    try:
        url = "https://oapi.dingtalk.com/gettoken"
        params = {
            "appkey": APP_KEY,
            "appsecret": APP_SECRET
        }
        
        resp = requests.get(url, params=params, timeout=10).json()
        
        if resp.get("errcode") == 0:
            ACCESS_TOKEN = resp["access_token"]
            EXPIRE_AT = time.time() + resp["expires_in"] - 200  # 提前 200 秒过期
            logger.info("钉钉 access_token 获取成功")
            return ACCESS_TOKEN
        else:
            logger.error(f"获取 access_token 失败: {resp}")
            return None
            
    except Exception as e:
        logger.error(f"获取 access_token 异常: {e}", exc_info=True)
        return None


def get_userid_by_mobile(mobile):
    """通过手机号获取 UserID"""
    try:
        access_token = get_access_token()
        if not access_token:
            return None
        
        url = "https://oapi.dingtalk.com/topapi/v2/user/getbymobile"
        params = {"access_token": access_token}
        data = {"mobile": mobile}
        
        resp = requests.post(url, params=params, json=data, timeout=10).json()
        
        if resp.get("errcode") == 0:
            userid = resp["result"]["userid"]
            logger.info(f"手机号 {mobile} 对应的 UserID: {userid}")
            return userid
        else:
            logger.error(f"获取 UserID 失败: {resp}")
            return None
            
    except Exception as e:
        logger.error(f"获取 UserID 异常: {e}", exc_info=True)
        return None


def format_time(iso_time_str):
    """格式化 ISO 8601 时间为友好格式"""
    try:
        if not iso_time_str:
            return "未知时间"
        time_str = iso_time_str.replace('Z', '+00:00')
        dt = datetime.fromisoformat(time_str)
        return dt.strftime('%Y-%m-%d %H:%M:%S')
    except Exception as e:
        logger.warning(f"时间格式化失败: {e}")
        return iso_time_str


def get_severity_emoji(severity):
    """根据严重程度返回对应的 emoji"""
    severity_map = {
        "critical": "🔴",
        "warning": "🟡",
        "info": "🔵",
    }
    return severity_map.get(severity.lower(), "⚪")


def get_status_info(status):
    """根据告警状态返回图标和文本"""
    if status == "resolved":
        return "✅", "已恢复"
    else:
        return "🔥", "告警中"


def send_work_notification(userid, alert_data):
    """发送工作通知到指定用户"""
    try:
        access_token = get_access_token()
        if not access_token:
            logger.error("无法获取 access_token，跳过发送")
            return False
        
        url = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2"
        params = {"access_token": access_token}
        
        # 构建 Markdown 消息
        status_icon = alert_data["status_icon"]
        alertname = alert_data["alertname"]
        status_text = alert_data["status_text"]
        severity_emoji = alert_data["severity_emoji"]
        severity = alert_data["severity"]
        alert_type = alert_data["alert_type"]
        instance = alert_data["instance"]
        current_value = alert_data["current_value"]
        threshold = alert_data["threshold"]
        start_time = alert_data["start_time"]
        description = alert_data["description"]
        
        # 构建消息内容
        content = f"""### {status_icon} {alertname} - {status_text}

**告警类型**: {alert_type}  
**严重程度**: {severity_emoji} {severity}  
**实例**: {instance}  
**状态**: {status_text}  
**当前值**: {current_value}  
**阈值**: {threshold}  
**开始时间**: {start_time}  

**详情**: {description}
"""
        
        # 如果已恢复，添加恢复时间
        if alert_data.get("end_time"):
            content += f"\n**恢复时间**: {alert_data['end_time']}"
        
        data = {
            "agent_id": 0,  # 企业内部应用的 AgentId，需要替换
            "userid_list": userid,
            "msg": {
                "msgtype": "markdown",
                "markdown": {
                    "title": f"{alertname} - {status_text}",
                    "text": content
                }
            }
        }
        
        resp = requests.post(url, params=params, json=data, timeout=10).json()
        
        if resp.get("errcode") == 0:
            logger.info(f"工作通知发送成功: {userid}")
            return True
        else:
            logger.error(f"工作通知发送失败: {resp}")
            return False
            
    except Exception as e:
        logger.error(f"工作通知发送异常: {e}", exc_info=True)
        return False


def format_alert_data(alert):
    """格式化告警数据"""
    labels = alert.get("labels", {})
    annotations = alert.get("annotations", {})
    status = alert.get("status", "firing")
    
    # 提取信息
    alertname = labels.get("alertname", "未知告警")
    severity = labels.get("severity", "warning")
    alert_type = labels.get("alert_type", "system")
    instance = annotations.get("instance") or labels.get("instance", "未知实例")
    description = annotations.get("description", "无描述")
    current_value = annotations.get("current_value", "N/A")
    threshold = annotations.get("threshold", "N/A")
    starts_at = alert.get("startsAt", "")
    ends_at = alert.get("endsAt", "")
    
    # 格式化时间
    start_time = format_time(starts_at)
    end_time = format_time(ends_at) if ends_at else None
    
    # 获取状态和严重程度图标
    status_icon, status_text = get_status_info(status)
    severity_emoji = get_severity_emoji(severity)
    
    return {
        "alertname": alertname,
        "severity": severity,
        "severity_emoji": severity_emoji,
        "alert_type": alert_type,
        "instance": instance,
        "description": description,
        "current_value": current_value,
        "threshold": threshold,
        "start_time": start_time,
        "end_time": end_time,
        "status_icon": status_icon,
        "status_text": status_text,
    }


def handle_alert(data):
    """处理 Prometheus 告警 Webhook"""
    try:
        if not data:
            logger.warning("收到空数据")
            return "No data", 400
        
        if not USER_IDS:
            logger.warning("未配置接收用户 UserID，跳过发送")
            return "ok"
        
        alerts = data.get("alerts", [])
        logger.info(f"收到 {len(alerts)} 条告警")
        
        success_count = 0
        failed_count = 0
        
        for alert in alerts:
            try:
                # 格式化告警数据
                alert_data = format_alert_data(alert)
                
                # 发送给每个用户
                for userid in USER_IDS:
                    if send_work_notification(userid, alert_data):
                        success_count += 1
                    else:
                        failed_count += 1
                    
                    # 避免发送过快
                    if len(USER_IDS) > 1:
                        time.sleep(0.3)
                        
            except Exception as e:
                logger.error(f"处理单条告警失败: {e}", exc_info=True)
                failed_count += 1
        
        logger.info(f"钉钉私聊消息发送完成: 成功 {success_count} 条, 失败 {failed_count} 条")
        
        return "ok"
        
    except Exception as e:
        logger.error(f"处理告警失败: {e}", exc_info=True)
        return f"Error: {str(e)}", 500
