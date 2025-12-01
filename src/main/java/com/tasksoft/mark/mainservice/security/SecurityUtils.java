package com.tasksoft.mark.mainservice.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getCurrentUserId() {
        return getPrincipal().getSubject();
    }

    public String getCurrentUserName() {
        return getPrincipal().getClaimAsString("preferred_username");
    }
}
