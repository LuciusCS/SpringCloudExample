

在 Spring Cloud 项目中，是否需要同时引入 spring-boot-starter-data-jpa 和 spring-boot-starter-data-jdbc 取决于具体场景：
1.依赖关系分析‌ 
spring-boot-starter-data-jpa 已包含 spring-boot-starter-data-jdbc 的传递依赖
JPA 实现（如 Hibernate）底层依赖 JDBC 操作数据库，因此默认会引入 JDBC 相关功能16。
‌显式声明 data-jdbc 的场景‌：
需要直接使用 JdbcTemplate 或原生 SQL 操作（绕过 JPA 抽象层）6
项目混合使用 JPA 和纯 JDBC 两种数据访问方式8