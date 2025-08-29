#!/bin/bash

# 参数校验
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "用法: $0 <开始时间> <结束时间>"
  exit 1
fi

START_TIME="$1"
STOP_TIME="$2"
BINLOG_DIR="/var/lib/mysql"  # binlog 所在路径
BINLOG_FILE_PATTERN="mysql-bin"

# 查找 binlog 文件
BINLOGS=$(ls -1 $BINLOG_DIR/$BINLOG_FILE_PATTERN.* | sort)

# 回放 binlog
for LOG in $BINLOGS; do
  echo "🌀 回放日志：$LOG"
  mysqlbinlog \
    --start-datetime="$START_TIME" \
    --stop-datetime="$STOP_TIME" \
    --read-from-remote-server \
    --user=root --password=你的密码 \
    $LOG | mysql -u root -p
done

echo "✅ 完成 binlog 回放。"