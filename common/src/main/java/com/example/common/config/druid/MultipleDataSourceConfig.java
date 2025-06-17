package com.example.common.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * Druid 中配置多数据源
 */
//
//@Configuration
//@EnableTransactionManagement
//public class MultipleDataSourceConfig {
//
//
//    @Bean(name = "primaryDataSource")
//    public DataSource primaryDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        // 配置数据库连接信息
//        dataSource.setUrl("jdbc:mysql://localhost:3306/primary_db");
//        dataSource.setUsername("primary_user");
//        dataSource.setPassword("primary_password");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        return dataSource;
//    }
//
//    @Bean(name = "secondaryDataSource")
//    public DataSource secondaryDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        // 配置数据库连接信息
//        dataSource.setUrl("jdbc:mysql://localhost:3306/secondary_db");
//        dataSource.setUsername("secondary_user");
//        dataSource.setPassword("secondary_password");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        return dataSource;
//    }
//
//    // Primary DataSource 事务管理器
//    @Bean(name = "primaryTransactionManager")
//    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    // Secondary DataSource 事务管理器
//    @Bean(name = "secondaryTransactionManager")
//    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    // 定义事务模板
//    @Bean(name = "primaryTransactionTemplate")
//    public TransactionTemplate primaryTransactionTemplate(@Qualifier("primaryTransactionManager") PlatformTransactionManager transactionManager) {
//        return new TransactionTemplate(transactionManager);
//    }
//
//    @Bean(name = "secondaryTransactionTemplate")
//    public TransactionTemplate secondaryTransactionTemplate(@Qualifier("secondaryTransactionManager") PlatformTransactionManager transactionManager) {
//        return new TransactionTemplate(transactionManager);
//    }
//}
