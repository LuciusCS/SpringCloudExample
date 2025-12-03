#!/bin/bash
# 告警问题诊断脚本

echo "========================================="
echo "  告警系统诊断"
echo "========================================="

echo ""
echo "1. 检查容器状态"
echo "-----------------------------------"
docker ps | grep -E "alert-webhook|alertmanager|prometheus"

echo ""
echo "2. 检查 alert-webhook 日志（最近 50 行）"
echo "-----------------------------------"
docker logs --tail 50 alert-webhook

echo ""
echo "3. 检查 Alertmanager 配置"
echo "-----------------------------------"
docker exec alertmanager cat /etc/alertmanager/config.yml

echo ""
echo "4. 检查当前活跃的告警"
echo "-----------------------------------"
curl -s http://localhost:9093/api/v2/alerts | python3 -m json.tool 2>/dev/null || echo "无法解析 JSON"

echo ""
echo "5. 测试短信 webhook 连通性"
echo "-----------------------------------"
curl -X POST http://localhost:5001/alert/sms \
  -H "Content-Type: application/json" \
  -d '{
    "alerts": [{
      "labels": {
        "alertname": "DiagnosticTest",
        "severity": "info",
        "instance": "test:9100"
      },
      "annotations": {
        "description": "诊断测试消息",
        "summary": "测试短信发送",
        "current_value": "N/A",
        "threshold": "N/A",
        "instance": "test-server"
      },
      "status": "firing",
      "startsAt": "2025-12-03T08:00:00Z"
    }]
  }'

echo ""
echo ""
echo "6. 检查 alert-webhook 最新日志"
echo "-----------------------------------"
docker logs --tail 20 alert-webhook

echo ""
echo "========================================="
echo "  诊断完成"
echo "========================================="
