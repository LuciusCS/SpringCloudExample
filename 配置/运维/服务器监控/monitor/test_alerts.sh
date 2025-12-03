#!/bin/bash
# test_alerts.sh - 触发 Prometheus 告警测试（匹配新的告警阈值）

echo "========================================="
echo "  Prometheus 告警系统测试"
echo "  新阈值: Warning 80% | Critical 90%"
echo "========================================="

# 检查是否安装 stress
if ! command -v stress &> /dev/null; then
    echo "❌ 未安装 stress 工具，请先安装："
    echo "   CentOS/RHEL: yum install -y stress"
    echo "   Ubuntu/Debian: apt-get install -y stress"
    exit 1
fi

# -----------------------
# 1️⃣ CPU 压力测试（目标：触发 80% 告警）
# -----------------------
echo ""
echo "===> 1. CPU 压力测试"
echo "     目标: 触发 HighCPUUsage (80%)"
echo "     持续时间: 4 分钟（需要持续 3 分钟才会触发告警）"

CPU_CORES=$(nproc)
echo "     CPU 核心数: $CPU_CORES"

# 使用所有核心，持续 240 秒（4分钟）
stress --cpu "$CPU_CORES" --timeout 240 &
CPU_PID=$!

echo "     ✅ CPU 压力测试已启动 (PID: $CPU_PID)"

# -----------------------
# 2️⃣ 内存压力测试（目标：触发 80% 告警）
# -----------------------
echo ""
echo "===> 2. 内存压力测试"
echo "     目标: 触发 HighMemoryUsage (80%)"
echo "     持续时间: 4 分钟（需要持续 3 分钟才会触发告警）"

TOTAL_MEM_KB=$(grep MemTotal /proc/meminfo | awk '{print $2}')
TOTAL_MEM_GB=$((TOTAL_MEM_KB / 1024 / 1024))

# 占用 85% 内存，确保超过 80% 阈值
MEM_BYTES=$((TOTAL_MEM_KB * 1024 * 85 / 100))

echo "     总内存: ${TOTAL_MEM_GB} GB"
echo "     目标占用: 85%"

stress --vm 2 --vm-bytes "$MEM_BYTES" --timeout 240 &
MEM_PID=$!

echo "     ✅ 内存压力测试已启动 (PID: $MEM_PID)"

# -----------------------
# 3️⃣ 磁盘压力测试（目标：根据实际情况调整）
# -----------------------
echo ""
echo "===> 3. 磁盘压力测试"
echo "     目标: 触发 HighDiskUsage (80%)"

# 获取根分区信息
ROOT_TOTAL=$(df -BG / | tail -1 | awk '{print $2}' | sed 's/G//')
ROOT_USED=$(df -BG / | tail -1 | awk '{print $3}' | sed 's/G//')
ROOT_AVAIL=$(df -BG / | tail -1 | awk '{print $4}' | sed 's/G//')
ROOT_PERCENT=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')

echo "     根分区总大小: ${ROOT_TOTAL} GB"
echo "     已使用: ${ROOT_USED} GB (${ROOT_PERCENT}%)"
echo "     可用: ${ROOT_AVAIL} GB"

# 计算需要填充多少才能达到 85%
TARGET_PERCENT=85
NEED_FILL=$((ROOT_TOTAL * TARGET_PERCENT / 100 - ROOT_USED))

if [ "$ROOT_PERCENT" -ge 80 ]; then
    echo "     ⚠️  当前使用率已超过 80%，跳过磁盘测试"
elif [ "$NEED_FILL" -le 0 ]; then
    echo "     ⚠️  磁盘空间充足，无需测试"
else
    echo "     需要填充: ${NEED_FILL} GB"
    
    # 限制最大填充 10GB，避免真的填满磁盘
    if [ "$NEED_FILL" -gt 10 ]; then
        NEED_FILL=10
        echo "     ⚠️  限制填充大小为 10 GB（安全考虑）"
    fi
    
    echo "     开始填充 ${NEED_FILL} GB..."
    dd if=/dev/zero of=/tmp/filldisk bs=1M count=$((NEED_FILL * 1024)) status=progress 2>&1 | tail -1 &
    DISK_PID=$!
    echo "     ✅ 磁盘填充已启动 (PID: $DISK_PID)"
fi

# -----------------------
# 等待压力测试完成
# -----------------------
echo ""
echo "========================================="
echo "  等待测试完成（约 4 分钟）..."
echo "========================================="
echo ""
echo "💡 提示："
echo "   - Warning 告警需要持续 3 分钟才会触发"
echo "   - Critical 告警需要持续 2 分钟才会触发"
echo "   - 您可以在 Prometheus 查看告警状态："
echo "     http://<your-ip>:9090/alerts"
echo "   - Alertmanager 查看告警："
echo "     http://<your-ip>:9093"
echo ""

# 等待 CPU 和内存测试完成
wait $CPU_PID $MEM_PID

if [ -n "$DISK_PID" ]; then
    wait $DISK_PID
fi

# -----------------------
# 清理临时文件
# -----------------------
echo ""
echo "===> 测试完成，清理临时文件"
if [ -f /tmp/filldisk ]; then
    rm -f /tmp/filldisk
    echo "     ✅ 已删除 /tmp/filldisk"
fi

echo ""
echo "========================================="
echo "  ✅ 告警测试完成！"
echo "========================================="
echo ""
echo "📊 预期结果："
echo "   1. CPU 使用率告警（HighCPUUsage）"
echo "   2. 内存使用率告警（HighMemoryUsage）"
echo "   3. 磁盘使用率告警（HighDiskUsage，如果触发）"
echo ""
echo "📱 检查通知："
echo "   - 钉钉群消息"
echo "   - 微信公众号消息"
echo "   - 短信通知"
echo ""
echo "🔍 查看日志："
echo "   docker logs alert-webhook"
echo ""
