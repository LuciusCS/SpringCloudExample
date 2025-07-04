#!/bin/bash

# 配置
BACKUP_BASE_DIR="/data/backups"
TODAY=$(date +%F)
TARGET_DIR="$BACKUP_BASE_DIR/incr_$TODAY"
MYSQL_USER="root"
MYSQL_PASS="你的密码"

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

xtrabackup --backup \
  --target-dir=$TARGET_DIR \
  --incremental-basedir=$INCR_BASE \
  --datadir=/var/lib/mysql \
  --user=$MYSQL_USER --password=$MYSQL_PASS

echo "【增量备份】完成！"