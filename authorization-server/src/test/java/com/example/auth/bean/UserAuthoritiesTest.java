package com.example.auth.bean;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthoritiesTest {

    @Test
    public void testGetAuthoritiesWithMultipleRoles() {
        User user = new User();

        // Role 1
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Permission p1 = new Permission();
        p1.setName("user:write");
        adminRole.getPermissions().add(p1);

        // Role 2
        Role userRole = new Role();
        userRole.setName("USER");
        Permission p2 = new Permission();
        p2.setName("user:read");
        userRole.getPermissions().add(p2);

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        user.setRoles(roles);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        Set<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertTrue(authorityNames.contains("ROLE_ADMIN"), "Should contain ROLE_ADMIN");
        assertTrue(authorityNames.contains("ROLE_USER"), "Should contain ROLE_USER");
        assertTrue(authorityNames.contains("user:write"), "Should contain user:write permission");
        assertTrue(authorityNames.contains("user:read"), "Should contain user:read permission");
        assertTrue(authorityNames.size() == 4, "Should contain exactly 4 authorities");
    }
}
