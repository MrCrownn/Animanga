package com.animanga.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                    // --- Public endpoints ---
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
                    .pathMatchers(HttpMethod.GET,
                        "/auth/api/roles",
                        "/auth/api/roles/**"
                    ).permitAll()

                    // --- ADMIN only ---
                    .pathMatchers("/auth/api/usuarios/**").hasRole("ADMIN")

                    // --- ADMIN or GESTOR: write operations on production, catalog, media, perfil, auditoria ---
                    .pathMatchers(HttpMethod.POST, "/produccion/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.PUT, "/produccion/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/produccion/api/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.POST, "/catalogo/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.PUT, "/catalogo/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/catalogo/api/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.POST, "/media/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.PUT, "/media/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/media/api/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.POST, "/perfil/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.PUT, "/perfil/api/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/perfil/api/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.GET, "/auditoria/api/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.POST, "/curacion/api/historial-curacion/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/curacion/api/historial-curacion/**").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.DELETE, "/curacion/api/propuestas/**").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers("/curacion/api/propuestas/{id}/estado").hasAnyRole("ADMIN", "GESTOR")

                    .pathMatchers(HttpMethod.PUT, "/soporte/api/incidencias/{id}/estado").hasAnyRole("ADMIN", "GESTOR")
                    .pathMatchers(HttpMethod.DELETE, "/soporte/api/incidencias/**").hasAnyRole("ADMIN", "GESTOR")

                    // --- Any authenticated user: user-write actions ---
                    .pathMatchers(HttpMethod.POST, "/curacion/api/propuestas").authenticated()
                    .pathMatchers(HttpMethod.POST, "/social/api/**").authenticated()
                    .pathMatchers(HttpMethod.POST, "/soporte/api/incidencias").authenticated()

                    // --- Everything else requires authentication ---
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
