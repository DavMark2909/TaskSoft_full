package com.tasksoft.mark.authservertasksoft.config;

import com.tasksoft.mark.authservertasksoft.security.SecurityUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (context.getTokenType().getValue().equals("access_token")) {
                Authentication principal = context.getPrincipal();
                context.getClaims().claim("username", principal.getName());

//                String authoritiesString = principal.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.joining(" "));
//                context.getClaims().claim("authority", authoritiesString);

                if (principal.getPrincipal() instanceof SecurityUser user) {
                    context.getClaims().claim("userId", user.getUser().getId().toString());
                    context.getClaims().claim("firstName", user.getUser().getFirstName());
                    context.getClaims().claim("lastName", user.getUser().getLastName());
                    context.getClaims().claim("role", user.getUser().getAuthorities());
                }
            }
        };
    }
}
