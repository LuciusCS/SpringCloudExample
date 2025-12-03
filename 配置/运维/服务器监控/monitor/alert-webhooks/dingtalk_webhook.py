#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests, json, time, hmac, hashlib, base64, urllib.parse


# 你的钉钉机器人配置
access_token = ""
secret = ""


def handle_alert(data):
    #data = request.get_json()
    if not data:
        return "No data", 400

    # 生成加签
    timestamp = str(round(time.time() * 1000))
    string_to_sign = f"{timestamp}\n{secret}"
    hmac_code = hmac.new(secret.encode("utf-8"), string_to_sign.encode("utf-8"), digestmod=hashlib.sha256).digest()
    sign = urllib.parse.quote_plus(base64.b64encode(hmac_code))

    # 钉钉 webhook
    url = f"https://oapi.dingtalk.com/robot/send?access_token={access_token}&timestamp={timestamp}&sign={sign}"

    alerts = data.get("alerts", [])
    for alert in alerts:
        name = alert["labels"].get("alertname")
        severity = alert["labels"].get("severity")
        desc = alert["annotations"].get("description")
        starts_at = alert.get("startsAt")

        msg = {
            "msgtype": "markdown",
            "markdown": {
                "title": f"{name} 告警",
                "text": f"### 告警名称：{name}\n\n"
                        f"**级别**：{severity}\n\n"
                        f"**详情**：{desc}\n\n"
                        f"**开始时间**：{starts_at}"
            }
        }
        try:
           response=requests.post(url, data=json.dumps(msg), headers={"Content-Type": "application/json"})
           print(f"钉钉返回的状态码: {response.status_code}")
           print(f"钉钉返回的错误信息: {response.text}")
        except Exception as e:
           print(f"发送钉钉消息失败: {e}")
    return "ok"


