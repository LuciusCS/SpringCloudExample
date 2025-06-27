package com.example.order.config.filter;


import com.example.order.filter.LoggingMDCFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingMDCFilter> loggingMDCFilter() {
        FilterRegistrationBean<LoggingMDCFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoggingMDCFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
