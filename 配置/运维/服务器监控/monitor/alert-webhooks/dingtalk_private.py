#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
钉钉私聊消息发送模块
使用钉钉机器人发送私聊消息到个人
"""

import time
import logging
from datetime import datetime
from alibabacloud_dingtalk.oauth2_1_0.client import Client as dingtalkoauth2_1_0Client
from alibabacloud_tea_openapi import models as open_api_models
from alibabacloud_dingtalk.oauth2_1_0 import models as dingtalkoauth_2__1__0_models
from alibabacloud_dingtalk.robot_1_0.client import Client as dingtalkrobot_1_0Client
from alibabacloud_dingtalk.robot_1_0 import models as dingtalkrobot__1__0_models
from alibabacloud_tea_util import models as util_models
from datetime import datetime, timedelta, timezone
import dateutil.parser  # 需要安装：pip install python-dateutil
# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# ======= 钉钉机器人配置 =======
# 需要在钉钉开发者后台获取
APP_KEY = ""      # 对应 Client ID
APP_SECRET = "-1"  # 对应 Client Secret
ROBOT_CODE = ""  # 对应机器人的 RobotCode

# 接收告警的用户 UserID 列表
# UserID 可以通过手机号查询获得 (注意：机器人API通常需要用户先与机器人发过消息才能发私聊)
USER_IDS = [
    ""
    # "user123",  # 取消注释并填写 UserID
]

# 缓存 access_token
_token_cache = {"token": None, "expire": 0}


def get_access_token():
    """
    使用钉钉SDK获取access_token，带本地缓存，2小时有效，提前200秒刷新。
    :return: access_token字符串，获取失败返回None
    """
    now = time.time()
    if _token_cache["token"] and now < _token_cache["expire"]:
        return _token_cache["token"]

    config = open_api_models.Config()
    config.protocol = 'https'
    config.region_id = 'central'
    client = dingtalkoauth2_1_0Client(config)
    get_access_token_request = dingtalkoauth_2__1__0_models.GetAccessTokenRequest(
        app_key=APP_KEY,
        app_secret=APP_SECRET
    )
    try:
        response = client.get_access_token(get_access_token_request)
        token = getattr(response.body, "access_token", None)
        expire_in = getattr(response.body, "expire_in", 7200)
        if token:
            _token_cache["token"] = token
            _token_cache["expire"] = now + expire_in - 200  # 提前200秒刷新
            logger.info("钉钉 access_token 获取成功")
        return token
    except Exception as err:
        logger.error(f"获取 access_token 失败: {err}")
        return None


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


def send_robot_private_message(userid, alert_data):
    """发送机器人私聊消息到指定用户"""
    access_token = get_access_token()
    if not access_token:
        logger.error("无法获取 access_token，跳过发送")
        return False

    # 构建消息内容
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

    # 转义内容以适配 JSON 字符串
    escaped_content = content.replace('"', '\\"').replace('\n', '\\n')
    
    # 使用 Markdown 格式发送
    msg_key = 'sampleMarkdown'
    msg_param = f'{{"text": "{escaped_content}", "title": "{alertname}"}}'

    config = open_api_models.Config()
    config.protocol = 'https'
    config.region_id = 'central'
    client = dingtalkrobot_1_0Client(config)

    batch_send_otoheaders = dingtalkrobot__1__0_models.BatchSendOTOHeaders()
    batch_send_otoheaders.x_acs_dingtalk_access_token = access_token
    batch_send_otorequest = dingtalkrobot__1__0_models.BatchSendOTORequest(
        robot_code=ROBOT_CODE,
        user_ids=[userid],
        msg_key=msg_key,
        msg_param=msg_param
    )
    
    try:
        response = client.batch_send_otowith_options(
            batch_send_otorequest,
            batch_send_otoheaders,
            util_models.RuntimeOptions()
        )
        # response 是一个对象，打印可能不直观，这里简单打印 body
        # logger.info(f"单聊消息发送成功，返回：{response.body}")
        logger.info(f"机器人私聊消息发送成功: {userid}")
        return True
    except Exception as err:
        logger.error(f"发送机器人私聊消息失败: {err}")
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
                    if send_robot_private_message(userid, alert_data):
                        success_count += 1
                    else:
                        failed_count += 1
                    
                    # 避免发送过快 (虽然机器人API可能有不同限流，但保留一点延时也是好的)
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

if __name__ == '__main__':
    # 简单测试
    print("Function 'handle_alert' is ready to be called by main.py or other callers.")
