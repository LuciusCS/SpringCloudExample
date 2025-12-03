
import requests, json, time


# ======= 微信服务号配置 =======
APP_ID = ""
APP_SECRET = ""
TEMPLATE_ID = ""

# 模拟 openId 列表（你可从数据库读取）
USER_LIST = [
    "otBDx6vw9xBm6rQO-"
]

# 缓存 access_token
ACCESS_TOKEN = None
EXPIRE_AT = 0


# 获取 access_token（自动缓存）
def get_access_token():
    global ACCESS_TOKEN, EXPIRE_AT

    if ACCESS_TOKEN and time.time() < EXPIRE_AT:
        return ACCESS_TOKEN

    url = f"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APP_ID}&secret={APP_SECRET}"
    resp = requests.get(url).json()

    if "access_token" not in resp:
        print("❌ 获取 access_token 失败：", resp)
        return None

    ACCESS_TOKEN = resp["access_token"]
    EXPIRE_AT = time.time() + resp["expires_in"] - 200

    print("✅ access_token 更新成功")
    return ACCESS_TOKEN


# 发送模板消息
def send_wechat_template(open_id, alert):
    access_token = get_access_token()
    if not access_token:
        return None

    url = f"https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={access_token}"

    data = {
        "touser": open_id,
        "template_id": TEMPLATE_ID,
        "data": {
          #  "first": {"value": "🔥 Prometheus 告警通知"},
          #  "time3": {"value": alert["start_time"]},
           "time3": {"value": "2022年11月22日 22:22:22"},
            "thing16": {"value": alert["severity"]},
            "thing2": {"value": alert["alertname"]},
            "thing46": {"value": alert["instance"]},
          #  "phrase20": {"value": alert["description"]},
 "phrase20": {"value": "严重"},
          #  "remark": {"value": "请尽快处理！（系统自动发送）"}
        }
    }

    resp = requests.post(url, json=data).json()
    print("消息推送:", resp)
    return resp



def handle_alert(data):

    alerts = data.get("alerts", [])

    for alert in alerts:
        labels = alert.get("labels", {})
        ann = alert.get("annotations", {})

        alert_data = {
            "alertname": labels.get("alertname", "N/A"),
            "severity": labels.get("severity", "N/A"),
            "instance": labels.get("instance", "N/A"),
            "description": ann.get("description", "无描述"),
            "start_time": alert.get("startsAt", ""),
        }

        # 推送到每位用户
        for uid in USER_LIST:
            send_wechat_template(uid, alert_data)

    return jsonify({"status": "ok"})



