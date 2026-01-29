package com.tasksoft.mark.mainservice.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getCurrentUserId() {
        Object userId = getPrincipal().getClaims().get("userId");
        if (userId == null) {
            throw new RuntimeException("Token is missing 'userId' claim");
        }

        if (userId instanceof Number number) {
            return number.longValue();
        }

        if (userId instanceof String string) {
            return Long.parseLong(string);
        }

        throw new RuntimeException("Invalid type for 'userId' claim: " + userId.getClass());
    }

    public String getCurrentUserName() {
        return getPrincipal().getClaimAsString("username");
    }

    public String getCurrentUserRole(){
        return getPrincipal().getClaimAsString("role");
    }


}
