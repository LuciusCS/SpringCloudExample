package com.example.order.config.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobConfig {
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(adminAddresses);  // 注入调度中心地址
        executor.setAddress("http://192.168.22.181:9999");  ///如果不显式添加，只在yml文件中进行配置，实际不会生效
        executor.setAccessToken("dsfds");
        executor.setAppname("order");   // 覆盖配置文件中的appname（可选）
        executor.setPort(9999);                      // 覆盖配置文件的端口
        return executor;
    }
}


