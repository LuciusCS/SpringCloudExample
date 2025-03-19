package com.example.auth.util;

import com.example.auth.bean.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsernamePasswordAuthenticationTokenDeserializer
        extends StdDeserializer<UsernamePasswordAuthenticationToken> {

    public UsernamePasswordAuthenticationTokenDeserializer() {
        super(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public UsernamePasswordAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // 提取Principal
        Object principal = parsePrincipal(node.get("principal"), ctxt);

        // 提取Credentials
        Object credentials = parseObject(node.get("credentials"), ctxt);

        // 提取Authorities
        Collection<? extends GrantedAuthority> authorities = parseAuthorities(node.get("authorities"));

        // 构建认证对象
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        token.setDetails(parseObject(node.get("details"), ctxt));

        return token;
    }

    private Object parsePrincipal(JsonNode node, DeserializationContext ctxt) throws IOException {
        if (node == null) return null;

        // 处理自定义User类型
        if (node.has("@class") && node.get("@class").asText().contains("User")) {
            return ctxt.readValue(node.traverse(), User.class);
        }
        return ctxt.readTreeAsValue(node, Object.class);
    }

    private Collection<? extends GrantedAuthority> parseAuthorities(JsonNode authoritiesNode) {
        if (authoritiesNode == null) return Collections.emptyList();
        return StreamSupport.stream(authoritiesNode.spliterator(), false)
                .map(n -> new SimpleGrantedAuthority(n.get("authority").asText()))
                .collect(Collectors.toList());
    }

    private Object parseObject(JsonNode node, DeserializationContext ctxt) throws IOException {
        return node != null ? ctxt.readTreeAsValue(node, Object.class) : null;
    }
}
