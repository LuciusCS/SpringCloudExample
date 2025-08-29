#!/bin/bash


# 配置
BACKUP_BASE_DIR="/develop/backup/full"
INCR_BASE="/develop/backup/full"
MYSQL_DATA_DIR="/develop/mysql/mysql4/"
TODAY=$(date +%F)
TARGET_DIR="$BACKUP_BASE_DIR/incr_$TODAY"
HOST="192.168.22.191"
PORT="33064"
MYSQL_USER="root"
MYSQL_PASS="123456"

# 找最近一次备份作为基准
BASE_DIR=$(find $BACKUP_BASE_DIR -type d -name 'base_*' | sort | tail -n 1)
LAST_INCR_DIR=$(find $BACKUP_BASE_DIR -type d -name 'incr_*' | sort | tail -n 1)

# 判断是否有增量可以接着用
if [[ "$LAST_INCR_DIR" > "$BASE_DIR" ]]; then
  INCR_BASE="$LAST_INCR_DIR"
else
  INCR_BASE="$BASE_DIR"
fi

echo "【增量备份】基于：$INCR_BASE → $TARGET_DIR"

xtrabackup --defaults-file=/develop/mysql/config/mysql4.cnf \
     --backup \
     --target-dir="$TARGET_DIR" \
     --incremental-basedir=$INCR_BASE \
     --datadir="$MYSQL_DATA_DIR" \
     --host="$HOST" \--port="$PORT" \
     --user="$MYSQL_USER" \
     --password="$MYSQL_PASS"

#--backup \
#  --target-dir=$TARGET_DIR \
#  --incremental-basedir=$INCR_BASE \
#  --datadir=/var/lib/mysql \
#  --user=$MYSQL_USER --password=$MYSQL_PASS



echo "【增量备份】完成！"