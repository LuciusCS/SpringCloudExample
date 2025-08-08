


## 多表查询

AreaPO、OperatorPO、StationPO ,
现在有 电站 → 区域 → 运营商 三张表，不使用  @ManyToOne / @OneToMany 等注解，
使用 使用 Criteria API  + DTO 进行多表联合查询并只返回某一张表的部分字段, 支
持 分页、过滤、以及排序的查询