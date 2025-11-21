#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from flask import Flask, request
import requests, json, time, hmac, hashlib, base64, urllib.parse

app = Flask(__name__)

# 你的钉钉机器人配置
access_token = ""
secret = ""

@app.route("/dingtalk", methods=["POST"])
def dingtalk_alert():
    data = request.get_json()
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

        requests.post(url, data=json.dumps(msg), headers={"Content-Type": "application/json"})

    return "ok"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)

