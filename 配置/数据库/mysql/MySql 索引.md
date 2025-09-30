
## MySql 索引失效的现象

1、✅ 执行计划显示全表扫描

使用 EXPLAIN 或 EXPLAIN ANALYZE：

```
EXPLAIN SELECT * FROM user WHERE name = 'zhao';

```
正常索引生效时：
type: ref / range / const
key: 非空（显示使用的索引）
rows: 很小（预估扫描行数）
Extra: 可能有 Using index（覆盖索引）
索引失效时：
type: ALL
key: NULL
rows: 非常大（扫描整个表）
Extra: Using where


## JPA 索引失效的常见原因及表现

| 场景                 | SQL 示例                            | 说明                |
| ------------------ | --------------------------------- | ----------------- |
| **模糊查询失效**         | `LIKE %zhao%`                     | `%`开头不能用索引        |
| **字段类型不匹配**        | `where id = '123'`（id 为 int）      | 隐式转换导致索引失效        |
| **函数包裹字段**         | `where year(createTime) = 2025`   | 函数使索引失效           |
| **OR条件混合索引列与非索引列** | `where name='zhao' or age=20`     | 索引合并失败            |
| **联合索引顺序错误**       | `where age = 20`（索引是 name, age）   | 不满足最左前缀           |
| **拼接 JPQL/HQL 拼错** | `... where u.name like '%:name%'` | 字符串拼接错误导致不能预编译或失效 |
| **动态查询拼接不全**       | `Specification` 动态拼接时漏掉某条件        | 无法使用联合索引          |


## MySql 索引失效排查

###  1. 使用 EXPLAIN 排查执行计划

首先，用 EXPLAIN（或 EXPLAIN ANALYZE，MySQL 8.0+）查看 SQL 的执行计划：

 ```
 EXPLAIN SELECT * FROM user WHERE name = 'Lucius';
 ```
重点看以下字段：

| 字段      | 含义                                              |
| ------- | ----------------------------------------------- |
| `type`  | 访问类型，理想的是 `const`、`ref`、`range`，不推荐 `ALL`（全表扫描） |
| `key`   | 实际使用的索引名                                        |
| `rows`  | 预估需要扫描的行数                                       |
| `extra` | 是否有 `Using index` 或 `Using where`               |

示例输出

```
type: ALL
key: NULL
rows: 100000
extra: Using where
```
说明：没有使用任何索引，扫描全表（索引失效）

###  2. 常见索引失效场景排查清单
#### ✅ 条件中使用了函数或表达式
```
-- 失效：索引列被包裹了函数
SELECT * FROM user WHERE DATE(create_time) = '2025-08-06';

-- 正确写法（范围条件）
SELECT * FROM user WHERE create_time >= '2025-08-06 00:00:00' AND create_time < '2025-08-07 00:00:00';

```

#### ✅ 字段类型不一致（隐式转换）
```
-- 假设 phone 是 VARCHAR 类型
-- 失效：传入数字导致隐式转换
SELECT * FROM user WHERE phone = 13800001234;

-- 正确写法
SELECT * FROM user WHERE phone = '13800001234';

```

#### ✅ 索引列前有 % 通配符
```
-- 失效
SELECT * FROM user WHERE name LIKE '%zhao';

-- 正确写法（右模糊才可用索引）
SELECT * FROM user WHERE name LIKE 'zhao%';

```

#### ✅ OR 多条件未全部命中索引
```
-- 失效：OR 两边不是都可使用索引
SELECT * FROM user WHERE name = 'zhao' OR age = 30;

-- 优化方式（拆成 UNION）
SELECT * FROM user WHERE name = 'zhao'
UNION
SELECT * FROM user WHERE age = 30;


```

#### ✅ 多列联合索引未遵循“最左前缀”

```
-- 联合索引 (name, age)
-- 失效：跳过了 name
SELECT * FROM user WHERE age = 30;

-- 正确用法
SELECT * FROM user WHERE name = 'zhao';
SELECT * FROM user WHERE name = 'zhao' AND age = 30;

```

#### ✅ 使用 IS NULL/IS NOT NULL 时是否支持视索引
IS NULL 可用索引
IS NOT NULL 一般不使用索引

### 3. 使用 SHOW INDEX FROM table 查看索引是否存在
```
SHOW INDEX FROM user;
```


### 4. 排查统计信息不准确（尤其是大表）
MySQL 的优化器依赖统计信息来判断是否使用索引，如果统计信息过期可能错误估算成本：
```
ANALYZE TABLE user;

```

### 5. 强制使用索引（仅排查用）

可以用 FORCE INDEX 指定索引，观察性能差异：
不建议在正式代码中长时间使用 FORCE INDEX，防止后续优化器判断被干扰。
```
SELECT * FROM user FORCE INDEX (idx_name) WHERE name = 'zhao';

```

### 6. 对比执行耗时和 profile
```
-- 开启 profile
SET profiling = 1;

-- 执行你的查询
SELECT * FROM user WHERE ...

-- 查看 profile
SHOW PROFILES;

```

### 索引失效排查流程图
```
      ↓
✔ EXPLAIN 查询执行计划
      ↓
✔ 检查是否使用了函数、类型转换、LIKE、OR 等
      ↓
✔ 检查索引设计和字段顺序（最左前缀）
      ↓
✔ SHOW INDEX 查看索引
      ↓
✔ ANALYZE TABLE 更新统计信息
      ↓
✔ 用 FORCE INDEX 验证优化器行为

```



### 索引支持比较、范围、like等信息吗

如果不支持，应该怎样进行处理2


### 空间数据索引(R-Tree) 

需要GIS相关函数支持，MySql支持的不好

