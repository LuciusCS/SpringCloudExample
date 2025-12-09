#!/bin/bash

set -e

echo "=============================="
echo "  🔥 Linux 全量告警触发脚本启动"
echo "=============================="

# ============ 1. CPU 高负载 (全核心) ============

echo "➡ 触发 CPU 高负载 (全核心) ..."
CPU_PIDS=""
CORES=$(nproc 2>/dev/null || echo 1)
echo "   检测到 $CORES 个 CPU 核心，正在启动压力进程..."

for i in $(seq 1 $CORES); do
  ( while :; do :; done ) &
  CPU_PIDS="$CPU_PIDS $!"
done

echo "   CPU 负载进程 PIDs: $CPU_PIDS"
sleep 5


# ============ 2. 内存吃满 (吃到 90% MemAvailable) ============

echo "➡ 触发 内存不足 (90% MemAvailable) ..."

# 计算可用内存的 90% (单位 MB)
MEM_AVAILABLE_MB=$(awk '/MemAvailable/{print int($2/1024 * 0.9)}' /proc/meminfo)
echo "   分配约 ${MEM_AVAILABLE_MB}MB 内存 ..."
MEM_STRESS_PID=""

(
  python3 - <<EOF
import time
# Convert MB to Bytes
size = $MEM_AVAILABLE_MB * 1024 * 1024
print("   Python 内存分配开始...")
try:
    a = bytearray(size)
    # 触摸内存以确保实际分配
    for i in range(0, size, 4096):
        a[i] = 1
    print("   内存分配完成，保持中...")
except Exception as e:
    print(f"   内存分配失败: {e}")
time.sleep(720)
EOF
) &
MEM_STRESS_PID=$!

sleep 5


# ============ 3. 磁盘占满 (free_bytes) ============

echo "➡ 触发 磁盘不足 (free_bytes)..."

DISK_STRESS_FILE="/tmp/disk_stress_test.bin"
FREE_DISK=$(df -k / | awk 'NR==2 {print $4}')
# 使用可用空间的 85% (df 输出是 1K-blocks)
USE_DISK=$((FREE_DISK * 85 / 100))

echo "   写入 ${USE_DISK}KB 到 $DISK_STRESS_FILE ..."
dd if=/dev/zero of=$DISK_STRESS_FILE bs=1K count=$USE_DISK status=none &
DISK_PID=$!
wait $DISK_PID

sleep 5


# ============ 4. 网络高流量 (非 lo 接口) ============

echo "➡ 启动网络高流量测试 (iperf3) use LAN IP..."

# 获取非 lo 的 IP 地址
TARGET_IP=$(hostname -I 2>/dev/null | awk '{print $1}')
if [ -z "$TARGET_IP" ]; then
    TARGET_IP=$(ip route get 1 2>/dev/null | awk '{print $7;exit}')
fi

if [ -z "$TARGET_IP" ] || [ "$TARGET_IP" = "127.0.0.1" ]; then
    echo "   ⚠️ 无法检测到局域网 IP，尝试使用 0.0.0.0 监听，但 client 可能无法走物理网卡"
    TARGET_IP="127.0.0.1"
fi

echo "   使用 IP: $TARGET_IP"

# Bind to the specific IP to force traffic through the stack
iperf3 -s -B $TARGET_IP >/dev/null 2>&1 &
IPERF_PID=$!

sleep 2

# Client connects to the IP (not localhost)
iperf3 -c $TARGET_IP -t 720 -P 4 >/dev/null 2>&1 &
IPERF_CLIENT_PID=$!

echo "   iperf3 server PID: $IPERF_PID"
echo "   iperf3 client PID: $IPERF_CLIENT_PID"

echo "➡ 告警触发测试进行中 (持续 12 分钟)..."
echo "   请观察 Prometheus 告警状态"

sleep 720


# ============ 5. 清理 ==================

echo "➡ 清理压力测试进程..."

# Kill all CPU loops
for pid in $CPU_PIDS; do
  kill $pid >/dev/null 2>&1 || true
done

kill $MEM_STRESS_PID >/dev/null 2>&1 || true
kill $IPERF_PID >/dev/null 2>&1 || true
kill $IPERF_CLIENT_PID >/dev/null 2>&1 || true

rm -f $DISK_STRESS_FILE

echo "=============================="
echo "  ✔ 测试结束，清理完成"
echo "=============================="
