package com.tasksoft.mark.gatewayv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyExchange().authenticated())
            .oauth2Login(oauth2 -> oauth2
                    // Force redirect to Frontend (localhost:5173) after login
                    .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:5173"))
            );

        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint((exchange, e) -> {
                String path = exchange.getRequest().getURI().getPath();

                // If the request is for the API, return 401 instead of redirecting
                if (path.startsWith("/api")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                return new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/gateway")
                    .commence(exchange, e);
            })
        );

        return http.build();
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("GATEWAY_SESSION");
        resolver.addCookieInitializer((builder) -> builder.secure(false));
        resolver.addCookieInitializer((builder) -> builder.sameSite("Lax"));

        return resolver;
    }
}
