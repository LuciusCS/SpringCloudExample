package com.example.order.config.filter;


import com.example.order.filter.LoggingMDCFilter;
import com.example.order.tenant.TenantContextWebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * 用于日志上下文打印的Filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<LoggingMDCFilter> loggingMDCFilter() {
        FilterRegistrationBean<LoggingMDCFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoggingMDCFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }


    /**
     * 用于处理 tenantId的Filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        // 创建 FilterRegistrationBean 对象并注册 TenantContextWebFilter
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());

        // 设置 URL 映射，可以根据需要调整
        registrationBean.addUrlPatterns("/order/*"); // 设置匹配的 URL 模式

        // 设置过滤器的顺序（数字越小，越先执行）
        registrationBean.setOrder(1);  // 你可以根据需要调整顺序

        return registrationBean;
    }

}
