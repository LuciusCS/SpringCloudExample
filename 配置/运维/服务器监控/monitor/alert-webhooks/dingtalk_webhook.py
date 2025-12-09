#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import time
import hmac
import hashlib
import base64
import urllib.parse
import logging
from datetime import datetime
from datetime import datetime, timedelta, timezone
import dateutil.parser  # 需要安装：pip install python-dateutil

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# 钉钉机器人配置
ACCESS_TOKEN = ""
SECRET = ""

# @ 提醒配置
# 方式1: @ 指定手机号的用户（推荐）
AT_MOBILES = [
    # "",  # 取消注释并填写手机号
]

# 方式2: @ 所有人
AT_ALL = False  # 设置为 True 则 @ 所有人

# 严重程度 @ 策略
# critical 级别的告警 @ 所有配置的人员
AT_ON_CRITICAL = True


def format_time(iso_time_str):
    """格式化 ISO 8601 时间为友好格式"""
    try:
        if not iso_time_str:
            return "未知时间"

        # 使用 dateutil.parser 自动解析各种ISO格式
        dt = dateutil.parser.isoparse(iso_time_str)

        # 如果解析出的时间没有时区，假定为UTC
        if dt.tzinfo is None:
            dt = dt.replace(tzinfo=timezone.utc)

        # 转换到北京时间
        beijing_tz = timezone(timedelta(hours=8))
        dt_beijing = dt.astimezone(beijing_tz)

        return dt_beijing.strftime("%Y-%m-%d %H:%M:%S")

    except Exception as e:
        logger.warning(f"时间格式化失败: {iso_time_str}, 错误: {e}")
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


def generate_dingtalk_sign():
    """生成钉钉机器人签名"""
    timestamp = str(round(time.time() * 1000))
    string_to_sign = f"{timestamp}\n{SECRET}"
    hmac_code = hmac.new(
        SECRET.encode("utf-8"),
        string_to_sign.encode("utf-8"),
        digestmod=hashlib.sha256
    ).digest()
    sign = urllib.parse.quote_plus(base64.b64encode(hmac_code))
    return timestamp, sign


def send_dingtalk_message(msg):
    """发送钉钉消息"""
    try:
        timestamp, sign = generate_dingtalk_sign()
        url = f"https://oapi.dingtalk.com/robot/send?access_token={ACCESS_TOKEN}&timestamp={timestamp}&sign={sign}"
        
        response = requests.post(
            url,
            data=json.dumps(msg),
            headers={"Content-Type": "application/json"},
            timeout=10
        )
        
        result = response.json()
        
        if result.get("errcode") == 0:
            logger.info(f"钉钉消息发送成功")
            return True
        else:
            logger.error(f"钉钉消息发送失败: {result}")
            return False
            
    except Exception as e:
        logger.error(f"钉钉消息发送异常: {e}", exc_info=True)
        return False


def format_alert_message(alert):
    """格式化告警为钉钉 Markdown 消息"""
    labels = alert.get("labels", {})
    annotations = alert.get("annotations", {})
    status = alert.get("status", "firing")
    
    # 提取信息
    alertname = labels.get("alertname", "未知告警")
    severity = labels.get("severity", "warning")
    alert_type = labels.get("alert_type", "system")
    instance = annotations.get("instance") or labels.get("instance", "未知实例")
    description = annotations.get("description", "无描述")
    summary = annotations.get("summary", alertname)
    current_value = annotations.get("current_value", "N/A")
    threshold = annotations.get("threshold", "N/A")
    starts_at = alert.get("startsAt", "")
    ends_at = alert.get("endsAt", "")
    
    # 格式化时间
    start_time = format_time(starts_at)
    
    # 获取状态和严重程度图标
    status_icon, status_text = get_status_info(status)
    severity_emoji = get_severity_emoji(severity)
    
    # 构建标题
    title = f"{status_icon} {alertname} - {status_text}"
    
    # 构建消息内容
    text_parts = [
        f"### {title}\n",
        f"**告警类型**: {alert_type}",
        f"**严重程度**: {severity_emoji} {severity}",
        f"**实例**: {instance}",
        f"**状态**: {status_text}",
        f"**当前值**: {current_value}",
        f"**阈值**: {threshold}",
        f"**开始时间**: {start_time}",
    ]
    
    # 如果已恢复，添加恢复时间
    if status == "resolved" and ends_at:
        end_time = format_time(ends_at)
        text_parts.append(f"**恢复时间**: {end_time}")
    
    # 添加描述
    text_parts.append(f"\n**详情**: {description}")
    
    # 组合消息
    text = "\n\n".join(text_parts)
    
    # 构建消息体
    msg = {
        "msgtype": "markdown",
        "markdown": {
            "title": title,
            "text": text
        }
    }
    
    # 添加 @ 功能
    at_config = {}
    
    # 根据严重程度决定是否 @
    should_at = False
    if severity.lower() == "critical" and AT_ON_CRITICAL:
        should_at = True
    elif AT_ALL:
        should_at = True
    elif len(AT_MOBILES) > 0:
        should_at = True
    
    if should_at:
        at_config = {
            "atMobiles": AT_MOBILES if not AT_ALL else [],
            "isAtAll": AT_ALL
        }
        msg["at"] = at_config
    
    return msg


def handle_alert(data):
    """处理 Prometheus 告警 Webhook"""
    try:
        if not data:
            logger.warning("收到空数据")
            return "No data", 400
        
        alerts = data.get("alerts", [])
        logger.info(f"收到 {len(alerts)} 条告警")
        
        success_count = 0
        failed_count = 0
        
        for alert in alerts:
            try:
                # 格式化消息
                msg = format_alert_message(alert)
                
                # 发送消息
                if send_dingtalk_message(msg):
                    success_count += 1
                else:
                    failed_count += 1
                
                # 避免发送过快
                if len(alerts) > 1:
                    time.sleep(0.5)
                    
            except Exception as e:
                logger.error(f"处理单条告警失败: {e}", exc_info=True)
                failed_count += 1
        
        logger.info(f"钉钉消息发送完成: 成功 {success_count} 条, 失败 {failed_count} 条")
        
        return "ok"
        
    except Exception as e:
        logger.error(f"处理告警失败: {e}", exc_info=True)
        return f"Error: {str(e)}", 500
