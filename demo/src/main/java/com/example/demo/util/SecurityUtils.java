package com.example.demo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     * 
     * @return userId
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            try {
                // Warning: This assumes the principal name is the User ID.
                // Adjust logic based on actual Authentication principal type (e.g. Jwt)
                String name = authentication.getName();
                return Long.valueOf(name);
            } catch (NumberFormatException e) {
                // If username is not a Long ID, you might need to look it up or handle
                // differently
                return 1L; // Fallback or throw
            }
        }
        return 1L; // Default for development/testing if not logged in
    }
}
