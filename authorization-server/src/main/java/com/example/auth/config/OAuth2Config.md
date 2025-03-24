


将 OAuth2 授权服务器的配置和 Spring Security 的配置分开，可以更清晰地分离关注点。
AuthorizationServerConfig 专注于授权服务器的配置，而 SecurityConfig 专注于如何保护资源服务器和认证、授权等安全控制。



在 Spring Security 6.1 中，oauth2ResourceServer(oauth2 -> oauth2.jwt()) 的 jwt() 方法已被弃用，并且标记为即将移




OpenID Connect 使用一个特殊的权限范围值 openid 来控制对 UserInfo 端点的访问。
OpenID Connect 定义了一组标准化的 OAuth 权限范围，对应于用户属性的子集profile、email、 phone、address，参见表格：


权限范围                     声明
openid                      sub
profile            Name、family_name、given_name、middle_name、nickname、preferred_username、profile、 picture、website、gender、birthdate、zoneinfo、locale、updated_at
email                   email、email_verified
address                 address,是一个 JSON 对象、包含 formatted、street_address、locality、region、postal_code、country
phone                   phone_number、phone_number_verified




//    在Spring开发中，允许跨域（CORS）通常是为了方便开发和调试的需要，而框架默认不允许跨域是出于安全性的考虑。下面详细解释这两个原因：
//
//    1. 开发时允许跨域的原因
//        a. 前后端分离开发
//            在现代的Web开发中，前端和后端往往是分离的。前端可能运行在一个不同的服务器或端口上，而后端API则在另一个服务器或端口上。这种情况下，前端在开发过程中需要访问后端的API，就会涉及跨域请求。
//        b. 便于调试
//            在开发阶段，允许跨域可以使开发者方便地进行前后端联调，快速验证和测试功能，而不必每次都考虑跨域问题。
//        c. 统一接口管理
//            开发过程中，前端开发者和后端开发者可能会频繁变更和测试接口。如果每次都因为跨域问题而进行额外配置，会增加开发的复杂性和时间成本。
//    2. 框架默认不允许跨域的原因
//        a. 安全性考虑
//           跨域资源共享涉及浏览器的同源策略（Same-Origin Policy），这是Web安全的基础。默认不允许跨域请求可以有效防止跨站请求伪造（CSRF）等安全攻击。
//        b. 防止数据泄露
//           默认不允许跨域请求可以防止恶意网站通过JavaScript向你的服务器发送请求，从而保护你的数据免受外部攻击。
//        c. 保护用户隐私
//            默认不允许跨域请求可以防止未经授权的域访问用户的敏感信息，保护用户隐私。
//   如何安全地配置跨域
//    在开发阶段可以宽松地配置CORS，但在生产环境中需要谨慎配置，通常建议：
//    限制允许的来源：只允许特定的可信来源进行跨域请求。
//    限制允许的方法：只允许必要的HTTP方法（如GET、POST等）。
//    限制允许的头信息：只允许必要的HTTP头信息。
//    启用凭据支持：根据需要启用或禁用凭据（cookies等）的传递，但要确保安全配置。
//    例如，在生产环境中，CORS配置可能会类似于以下代码

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://trustedwebsite.com"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true); // 根据需要启用或禁用
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
