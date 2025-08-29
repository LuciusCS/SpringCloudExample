#!/bin/bash

# 配置部分
BACKUP_BASE_DIR="/develop/backup/full"
TODAY=$(date +%F)
TARGET_DIR="$BACKUP_BASE_DIR/base_$TODAY"
MYSQL_DATA_DIR="/develop/mysql/mysql4/"
HOST="192.168.22.191"
PORT="33064"
MYSQL_USER="root"
MYSQL_PASS="123456"


echo "【全量备份】开始 $TODAY ..."

## 这里的 --defaults-file 可以另外建一个,不用使用 mysql4.cnf，因为备份使用的是宿主机的路径，而mysql4.cn中使用的是 Docker 容器中的路径
xtrabackup --defaults-file=/develop/mysql/config/mysql4.cnf \
     --backup \
     --target-dir="$TARGET_DIR" \
     --datadir="$MYSQL_DATA_DIR" \
     --host="$HOST" \--port="$PORT" \
     --user="$MYSQL_USER" \
     --password="$MYSQL_PASS"

echo "【全量备份】完成！目录：$TARGET_DIR"