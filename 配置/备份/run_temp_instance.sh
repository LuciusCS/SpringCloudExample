#!/bin/bash

# 配置部分
RESTORE_DIR="/data/tmp_restore"    # 已 prepare 完的恢复目录
TEMP_PORT=3307                     # 临时 MySQL 实例的端口
SOCKET_FILE="/tmp/mysql_restore.sock"  # 临时 socket 文件路径
MYSQLD_BIN="$(which mysqld)"       # mysqld 的位置

# 确保权限正确
chown -R mysql:mysql "$RESTORE_DIR"

# 启动 MySQL 实例
$MYSQLD_BIN \
  --datadir=$RESTORE_DIR \
  --port=$TEMP_PORT \
  --socket=$SOCKET_FILE \
  --pid-file=/tmp/mysql_restore.pid \
  --server-id=9999 \
  --skip-networking=0 \
  --log-error=/tmp/mysql_restore.err \
  --tmpdir=/tmp \
  --skip-grant-tables &

# 等待 MySQL 实例启动
sleep 5

# 检查是否成功启动
if netstat -an | grep -q ":$TEMP_PORT"; then
  echo "✅ 临时 MySQL 实例已启动：端口 $TEMP_PORT"
else
  echo "❌ 启动失败，请检查 /tmp/mysql_restore.err"
  exit 1
fi