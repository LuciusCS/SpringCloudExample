#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
import dingtalk_webhook
import wechat_webhook
import ali_send_sms

app = Flask(__name__)

@app.route("/")
def index():
    return "Alert Webhooks Service Running"

@app.route("/alert/dingtalk", methods=["POST"])
def alert_dingtalk():
    data = request.json
    result = dingtalk_webhook.handle_alert(data)
    return jsonify({"status": "ok", "result": result})

@app.route("/alert/wechat", methods=["POST"])
def alert_wechat():
    data = request.json
    result = wechat_webhook.handle_alert(data)
    return jsonify({"status": "ok", "result": result})

@app.route("/alert/sms", methods=["POST"])
def alert_sms():
    data = request.json
    result = ali_send_sms.handle_alert(data)
    return jsonify({"status": "ok", "result": result})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
