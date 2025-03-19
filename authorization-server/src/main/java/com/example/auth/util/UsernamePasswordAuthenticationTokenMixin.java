package com.example.auth.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// Mixin for UsernamePasswordAuthenticationToken
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class UsernamePasswordAuthenticationTokenMixin {
    @JsonCreator
    public UsernamePasswordAuthenticationTokenMixin(@JsonProperty("principal") Object principal,
                                                    @JsonProperty("credentials") Object credentials,
                                                    @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    }
}