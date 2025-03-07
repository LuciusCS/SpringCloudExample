package com.example.auth.config.extension.password;

import com.example.auth.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PasswordAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        // 授权类型 (必需)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }

        // 客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 参数提取验证
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // 令牌申请访问范围验证 (可选)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 用户名验证(必需)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
//        if (StrUtil.isBlank(username)) {
//            OAuth2EndpointUtils.throwError(
//                    OAuth2ErrorCodes.INVALID_REQUEST,
//                    OAuth2ParameterNames.USERNAME,
//                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI
//            );
//        }

        // 密码验证(必需)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
//        if (StrUtil.isBlank(password)) {
//            OAuth2EndpointUtils.throwError(
//                    OAuth2ErrorCodes.INVALID_REQUEST,
//                    OAuth2ParameterNames.PASSWORD,
//                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI
//            );
//        }

        // 附加参数(保存用户名/密码传递给 PasswordAuthenticationProvider 用于身份认证)
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) &&
                        !e.getKey().equals(OAuth2ParameterNames.SCOPE)
                ).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        return new PasswordAuthenticationToken(
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }

}
