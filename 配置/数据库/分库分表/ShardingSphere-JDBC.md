


ShardingSphere-JDBC 推荐用 HikariCP / 内置池，不强制用 Druid。
但如果你已经在用 Spring Boot + Druid，完全可以继续用 Druid 来接管连接池，只要把数据源 type 配成 Druid 就行


```shell

spring:
  shardingsphere:
    datasource:
      names: ds0,ds1

      ds0:
        type: com.zaxxer.hikari.HikariDataSource
        # type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://router-vip:6446/db0?serverTimezone=UTC&useSSL=false
        username: root
        password: 123456

      ds1:
        type: com.zaxxer.hikari.HikariDataSource
        #      type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://router-vip:6446/db1?serverTimezone=UTC&useSSL=false
        username: root
        password: 123456

```