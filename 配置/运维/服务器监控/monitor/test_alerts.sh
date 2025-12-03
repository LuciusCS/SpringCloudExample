#!/bin/bash
# test_alerts.sh - 一键触发 Prometheus 告警（CPU / 内存 / 磁盘）

# -----------------------
# 1️⃣ CPU 压力测试
# -----------------------
echo "==> 开始 CPU 压力测试（持续 180 秒）"
CPU_CORES=$(nproc)
stress --cpu "$CPU_CORES" --timeout 180 &
CPU_PID=$!

# -----------------------
# 2️⃣ 内存压力测试
# -----------------------
echo "==> 开始内存压力测试（占用 70% 系统内存，持续 180 秒）"
TOTAL_MEM_KB=$(grep MemTotal /proc/meminfo | awk '{print $2}')
# 70% 内存换算成字节数
MEM_BYTES=$((TOTAL_MEM_KB * 1024 * 70 / 100))
stress --vm 2 --vm-bytes "$MEM_BYTES" --timeout 180 &
MEM_PID=$!

# -----------------------
# 3️⃣ 磁盘压力测试（占 5GB 临时文件）
# -----------------------
echo "==> 开始磁盘压力测试（生成 /tmp/filldisk 5GB）"
dd if=/dev/zero of=/tmp/filldisk bs=1M count=5120 status=progress &

DISK_PID=$!

# -----------------------
# 等待压力测试完成
# -----------------------
wait $CPU_PID $MEM_PID $DISK_PID

# -----------------------
# 清理临时文件
# -----------------------
echo "==> 测试完成，清理临时文件"
rm -f /tmp/filldisk

echo "==> CPU / 内存 / 磁盘告警触发完成，请在 Prometheus / Alertmanager 查看 FIRING 状态"
