#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
import dingtalk_webhook
import dingtalk_private
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
    # dingtalk returns string or tuple
    if isinstance(result, tuple):
        return jsonify({"status": "error", "message": result[0]}), result[1]
    return jsonify({"status": "ok", "result": result})

@app.route("/alert/dingtalk-private", methods=["POST"])
def alert_dingtalk_private():
    data = request.json
    result = dingtalk_private.handle_alert(data)
    if isinstance(result, tuple):
        return jsonify({"status": "error", "message": result[0]}), result[1]
    return jsonify({"status": "ok", "result": result})

@app.route("/alert/wechat", methods=["POST"])
def alert_wechat():
    data = request.json
    # wechat returns jsonify object (Response)
    return wechat_webhook.handle_alert(data)

@app.route("/alert/sms", methods=["POST"])
def alert_sms():
    data = request.json
    # sms returns jsonify object (Response)
    return ali_send_sms.handle_alert(data)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
