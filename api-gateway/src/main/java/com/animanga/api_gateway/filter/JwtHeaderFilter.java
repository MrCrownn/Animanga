package com.animanga.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
            .filter(p -> p instanceof JwtAuthenticationToken)
            .cast(JwtAuthenticationToken.class)
            .map(auth -> {
                Jwt jwt = auth.getToken();
                ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-Id", jwt.getClaimAsString("id"))
                    .header("X-User-Username", jwt.getClaimAsString("username"))
                    .header("X-User-Rol", jwt.getClaimAsString("rol"))
                    .build();
                return exchange.mutate().request(request).build();
            })
            .defaultIfEmpty(exchange)
            .flatMap(chain::filter);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
