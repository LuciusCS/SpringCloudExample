package com.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置, 用于允许部分请求，不带权限校验
 *
 */
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    /** 免登录请求 */
    private List<Request> permitAll = new ArrayList<>();

    @Data
    public static class Request {

        /** 说明 */
        private String desc;

        /** 请求方式，空表示所有请求 */
        private HttpMethod method;

        /** 请求链接地址，如 "/manager/user/1" */
        private String uri;

    }

    public enum HttpMethod {

        GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE

    }

}
