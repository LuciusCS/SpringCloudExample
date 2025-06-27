# 使用ElasticSearch 保存 Product

不需要提前在 Elasticsearch 中建表。相较于关系型数据库，Elasticsearch 不需要显式地创建表。
在 Spring Data Elasticsearch 中，索引会在第一个文档被索引时自动创建，
并且可以通过 @Document 注解指定索引名。字段会自动映射为 Elasticsearch 中的 字段，并且支持动态映射。