#!/bin/bash

# 参数校验
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "用法: $0 <表名> <数据库名>"
  exit 1
fi

TABLE_NAME="$1"
DB_NAME="$2"
DUMP_FILE="/tmp/${TABLE_NAME}_dump.sql"

# 临时实例的 socket 和端口配置
SOCKET_FILE="/tmp/mysql_restore.sock"
PORT=3307

echo "🔄 从临时恢复实例导出表：$TABLE_NAME"

mysqldump -S $SOCKET_FILE -P $PORT -u root $DB_NAME $TABLE_NAME > $DUMP_FILE

if [ $? -eq 0 ]; then
  echo "✅ 表已导出：$DUMP_FILE"
else
  echo "❌ 导出失败"
fi