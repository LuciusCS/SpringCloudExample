package com.example.order.config.security;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 配置 URL 的安全配置
     * <p>
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */

    /// 资源服务器配置 (defaultSecurityFilterChain) 是专门保护应用资源的配置，主要处理对 API 的授权访问，确保客户端请求中携带有效的 JWT 或其他类型的 OAuth2 令牌。
    /// 其实没必要在授权服务器中添加
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                //参考资源服务器中的配置
                .securityMatcher(request ->
                                !request.getRequestURI().startsWith("/oauth2/") // 排除 /oauth2/ 路径的请求，使其不被当前的过滤器处理。
                        // 这意味着该过滤器不会处理与 OAuth2 授权相关的请求，主要保护其他资源。

                )
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/images/**", "/favicon.ico").permitAll()
                                .requestMatchers("/oauth2/jwks", "/.well-known/openid-configuration", "/druid/**", /// 表示上述端点可以被任何人进行访问
                                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/order/**"
//                                "/oauth2/authorize" // 允许匿名访问授权端点（触发登录）
                                ).permitAll()
                                .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/.well-known/openid-configuration", "/addRegisteredClient", "/oauth2/token", "/addUser", "/druid" +
                                "/**", "/order/**")
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())
//                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 确保 OAuth2 的过期 token 也会交由 CustomAuthenticationEntryPoint 处理
                        ));

        return http.build();
    }

    /**
     * 它定义了 Gateway（或资源服务器）如何解析和校验 JWT Token 的方式。
     * http://127.0.0.1:8003/oauth2/jwks 是启动authorization-server 后的启动地址
     *
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("http://127.0.0.1:8003/oauth2/jwks").build();
    }


    /// 用于处理不需要进行鉴权的接口
    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class) // 方法级
                    && !handlerMethod.getBeanType().isAnnotationPresent(PermitAll.class)) { // 接口级
                continue;
            }
            Set<String> urls = new HashSet<>();
            if (entry.getKey().getPatternsCondition() != null) {
                urls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                /// 最终需要取消注释，下面的代码还是有用的
//                urls.addAll(convertList(entry.getKey().getPathPatternsCondition().getPatterns(), PathPattern::getPatternString));
            }
            if (urls.isEmpty()) {
                continue;
            }

            // 特殊：使用 @RequestMapping 注解，并且未写 method 属性，此时认为都需要免登录
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();
            /// 最终需要取消注释，下面的代码还是有用的

//            if (CollUtil.isEmpty(methods)) {
//                result.putAll(HttpMethod.GET, urls);
//                result.putAll(HttpMethod.POST, urls);
//                result.putAll(HttpMethod.PUT, urls);
//                result.putAll(HttpMethod.DELETE, urls);
//                result.putAll(HttpMethod.HEAD, urls);
//                result.putAll(HttpMethod.PATCH, urls);
//                continue;
//            }
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                    case HEAD:
                        result.putAll(HttpMethod.HEAD, urls);
                        break;
                    case PATCH:
                        result.putAll(HttpMethod.PATCH, urls);
                        break;
                }
            });
        }
        return result;
    }
}
