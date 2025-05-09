package com.example.common.config;

import com.example.common.config.BasicDataSourceCfg;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

/**
 * JpaConfig 配置应该放在每个微服务中
 * 不应该在Common模块中进行
 *
 */
// other import statements omitted
@Import(BasicDataSourceCfg.class)
@Configuration
@EnableTransactionManagement
/**
 * 如果你没有使用 @ComponentScan，Spring 容器只会扫描配置类所在的包及其子包中的组件，
 * 其他包中的类就不会被自动扫描到。因此，如果你希望在 common 模块中进行配置，
 * 并且希望服务层（如 service 模块）的 Bean 也能够生效，@ComponentScan 就是必须的。
 *
 */
@ComponentScan(basePackages = {" com.apress.prospring6.eight.service"})
public class JpaConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(JpaConfig.class);
    @Autowired
    DataSource dataSource;
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager=new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    @Bean
    public Properties jpaProperties() {
        Properties jpaProps = new Properties();
        jpaProps.put(Environment.HBM2DDL_AUTO, "none");
        jpaProps.put(Environment.FORMAT_SQL, false);
        jpaProps.put(Environment.USE_SQL_COMMENTS, false);
        jpaProps.put(Environment.SHOW_SQL, false);
        return jpaProps;
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        //factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setPackagesToScan("com.apress.prospring6.eight.entities");
        factory.setJpaProperties(jpaProperties());
        return factory;
    }
}