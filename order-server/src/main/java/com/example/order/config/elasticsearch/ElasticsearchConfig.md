

Deprecated in 7.15.0.
`The High Level REST Client is deprecated in favour of the Java API Client.`


Elasticsearch 8.x已正式弃用High Level REST Client (HLRC)，并移除了Java Transport Client，spring-boot-starter-elasticsearch 已经不推荐使用




特性          	    spring-boot-starter-data-elasticsearch	                  spring-boot-starter-elasticsearch 
抽象层级                  	高级抽象，基于 Spring Data	                      低级别，直接操作 Elasticsearch 客户端
适用场景	                        简单的 CRUD 操作、快速开发	                          复杂的查询、高级操作、精细控制
查询方式            	支持通过 ElasticsearchRepository 定义查询，自动生成          	需要手动构建查询，更多的灵活性和控制
依赖项	                依赖 Spring Data，自动生成 Repository 接口	            依赖 RestClient 或 Java API Client，直接操作 ES
扩展性与灵活性             	较低，限制于 Spring Data 提供的功能                    	非常高，支持所有 Elasticsearch 功能
学习曲线                	较低，Spring Data 提供了很多自动化功能	                    较高，需要理解 Elasticsearch 的底层操作
事务管理	        支持 Spring 的事务管理，但 Elasticsearch 并不支持传统的事务	不支持事务管理，Elasticsearch 本身是一个 NoSQL 系统
配置	                配置较为简洁，主要依赖 Spring Boot 的自动配置               	需要更多手动配置，例如 Elasticsearch 客户端的配置


```


    <!-- Spring Boot Starter Data Elasticsearch -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>

<!-- 直接使用 Elasticsearch Java Client（如 RestHighLevelClient 或 Java API Client）也是可以的。 -->
<!-- spring-boot-starter-elasticsearch（它默认集成了 RestHighLevelClient），官方已经不推荐使用 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-elasticsearch</artifactId>
</dependency>

```

要替代 RestHighLevelClient，Elasticsearch 官方推荐使用 Elasticsearch Java Client
Elasticsearch Java Client 依赖：

```
<dependencies>
    <!-- Elasticsearch Java Client -->
    <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>elasticsearch-java</artifactId>
        <version>8.0.0</version> <!-- 这里的版本号根据你使用的 Elasticsearch 版本调整 -->
    </dependency>
</dependencies>

```

spring-boot-starter-data-elasticsearch (从 Spring Boot 2.4.x / Spring Data Elasticsearch 4.x 版本开始)
默认使用的是基于 RestHighLevelClient 的客户端，但在 Spring Data Elasticsearch 4.3+ / Spring Boot 2.5+之后，
其底层实际上依赖并封装了elasticsearch-java（当时称为 Java API Client）‌ 或兼容的客户端
因此，在较新的Spring Boot版本（如2.7.x或3.x）中配置连接，会自动为 elasticsearch-java 客户端准备好基础。


## 使用spring-boot-starter-data-elasticsearch 
elasticsearch的配置方式

```
spring:
      elasticsearch:
        uris: http://192.168.22.180:9200  # HTTP 地址（可多个） Elasticsearch 8.x 必须使用 uris 方式

```