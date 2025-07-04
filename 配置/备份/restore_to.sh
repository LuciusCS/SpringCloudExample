#!/bin/bash

# 参数校验
if [ -z "$1" ]; then
  echo "用法: $0 <目标日期，如 2025-06-04>"
  exit 1
fi

TARGET_DATE="$1"
BACKUP_DIR="/data/backups"
RESTORE_DIR="/data/tmp_restore"

echo "🔁 恢复到日期：$TARGET_DATE"

# 1. 清空旧恢复目录
rm -rf $RESTORE_DIR
mkdir -p $RESTORE_DIR

# 2. 找全量目录
BASE=$(find $BACKUP_DIR -maxdepth 1 -type d -name 'base_*' | sort | grep "$TARGET_DATE" -m1)

if [ -z "$BASE" ]; then
  BASE=$(find $BACKUP_DIR -maxdepth 1 -type d -name 'base_*' | sort | head -n 1)
  echo "⚠️ 未找到匹配日期的全量备份，使用最近的：$BASE"
fi

cp -a $BASE/* $RESTORE_DIR

# 3. 增量合并
INCR_LIST=$(find $BACKUP_DIR -maxdepth 1 -type d -name "incr_*" | sort | while read INCR; do
  [[ "$(basename $INCR | cut -d_ -f2)" < "$TARGET_DATE" || "$(basename $INCR | cut -d_ -f2)" = "$TARGET_DATE" ]] && echo $INCR
done)

# 4. 执行 apply-log
for INCR in $INCR_LIST; do
  echo "➕ 合并增量：$INCR"
  xtrabackup --prepare \
    --apply-log-only \
    --target-dir=$RESTORE_DIR \
    --incremental-dir=$INCR
done

# 5. 最后一次完整 apply-log（去掉 apply-log-only）
xtrabackup --prepare --target-dir=$RESTORE_DIR

echo "✅ 恢复目录已准备好：$RESTORE_DIR"
echo "请手动替换 /var/lib/mysql 数据目录，并启动 MySQL"