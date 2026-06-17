package com.animanga.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/auth/api/usuarios/login",
                            "/auth/api/usuarios/registro",
                            "/auth/swagger-ui/**",
                            "/auth/swagger-ui.html",
                            "/auth/v3/api-docs/**",
                            "/catalogo/swagger-ui/**",
                            "/catalogo/swagger-ui.html",
                            "/catalogo/v3/api-docs/**",
                            "/perfil/swagger-ui/**",
                            "/perfil/swagger-ui.html",
                            "/perfil/v3/api-docs/**",
                            "/produccion/swagger-ui/**",
                            "/produccion/swagger-ui.html",
                            "/produccion/v3/api-docs/**",
                            "/auditoria/swagger-ui/**",
                            "/auditoria/swagger-ui.html",
                            "/auditoria/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .build();
    }
}