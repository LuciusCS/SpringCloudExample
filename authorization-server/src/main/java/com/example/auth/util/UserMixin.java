package com.example.auth.util;

import com.example.auth.bean.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@JsonIgnoreType
public abstract class UserMixin {

    @JsonCreator
    public UserMixin(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("email") String email,
            @JsonProperty("roles") Set<Role> roles
    ) {}

    @JsonIgnore
    public abstract Collection<? extends GrantedAuthority> getAuthorities();

    @JsonIgnore
    public abstract boolean isAccountNonExpired();

    @JsonIgnore
    public abstract boolean isAccountNonLocked();

    @JsonIgnore
    public abstract boolean isCredentialsNonExpired();
}
