#!/bin/bash

# 配置部分
BACKUP_BASE_DIR="/data/backups"
TODAY=$(date +%F)
TARGET_DIR="$BACKUP_BASE_DIR/base_$TODAY"
MYSQL_USER="root"
MYSQL_PASS="你的密码"

echo "【全量备份】开始 $TODAY ..."

xtrabackup --backup \
  --target-dir=$TARGET_DIR \
  --datadir=/var/lib/mysql \
  --user=$MYSQL_USER --password=$MYSQL_PASS

echo "【全量备份】完成！目录：$TARGET_DIR"