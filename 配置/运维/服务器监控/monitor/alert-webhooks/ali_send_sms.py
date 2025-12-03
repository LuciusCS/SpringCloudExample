import json
import logging
import time

import requests
from aliyunsdkdysmsapi.request.v20170525 import SendSmsRequest
from aliyunsdkcore.client import AcsClient

# 阿里云短信配置
ACCESS_KEY_ID = ""
ACCESS_KEY_SECRET = ""
SIGN_NAME = ""
TEMPLATE_CODE = ""  #复电短信



# 创建阿里云短信客户端
def create_aliyun_client():
    return AcsClient(ACCESS_KEY_ID, ACCESS_KEY_SECRET, "cn-hangzhou")

# 发送短信的功能
def send_sms(phone_numbers, message):
    client = create_aliyun_client()

    # 创建短信请求
    request = SendSmsRequest.SendSmsRequest()
    request.set_TemplateCode(TEMPLATE_CODE)
    request.set_SignName(SIGN_NAME)
    request.set_PhoneNumbers(phone_numbers)
    
    # 设置短信模板参数
    request.set_TemplateParam(json.dumps(message))  # 将告警信息转为 JSON 格式
    
    try:
        # 发送短信请求
        response = client.do_action_with_exception(request)
        response_dict = json.loads(response.decode('utf-8'))
        if response_dict.get('Code') == 'OK':
            logging.info(f"短信发送成功: {phone_numbers}")
        else:
            logging.error(f"短信发送失败: {response_dict}")
    except Exception as e:
        logging.error(f"短信发送失败: {e}")

# 格式化告警信息
def format_alert_message(alert):
    return {
        "name": alert.get("alertname", "N/A"),
    #   "severity": alert.get("severity", "N/A"),
        "address": alert.get("instance", "N/A"),
        "date": "2025-10-28 16:00:00",
        "event": alert.get("description", "无描述"),
    #   "start_time": alert.get("startsAt", ""),
    }

# 处理 Prometheus 告警 Webhook

def handle_alert(data):

    alerts = data.get("alerts", [])

    for alert in alerts:
        # 格式化告警数据
        alert_data = format_alert_message(alert)

        # 发送短信到目标手机号（可以设置为多个手机号）
        phone_numbers = ""  # 目标手机号，可以是多个手机号，逗号分隔
        send_sms(phone_numbers, alert_data)

    return jsonify({"status": "ok"}), 200

