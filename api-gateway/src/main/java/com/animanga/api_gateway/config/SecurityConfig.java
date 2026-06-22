package com.animanga.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Flux;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                    .pathMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/*/v3/api-docs"
                    ).permitAll()

                    .pathMatchers(
                        "/auth/api/usuarios/login",
                        "/auth/api/usuarios/registro"
                    ).permitAll()

                    .pathMatchers(
                        "/auth/api/roles",
                        "/auth/api/roles/**"
                    ).permitAll()

                    .pathMatchers("/auth/api/usuarios/{id}/rol").hasRole("ADMIN")
                    .pathMatchers("/curacion/propuestas/{id}/estado").hasAnyRole("ADMIN", "GESTOR")

                    .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String rol = jwt.getClaimAsString("rol");
            if (rol == null) {
                return Flux.empty();
            }
            return Flux.just(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
        });
        return converter;
    }
}
