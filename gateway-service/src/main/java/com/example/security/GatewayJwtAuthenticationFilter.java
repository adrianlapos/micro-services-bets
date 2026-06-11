package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;

@Component
public class GatewayJwtAuthenticationFilter implements GlobalFilter {

    private final JwtGatewayValidator jwtGatewayValidator;

    public GatewayJwtAuthenticationFilter(JwtGatewayValidator jwtGatewayValidator) {
        this.jwtGatewayValidator = jwtGatewayValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("FORWARDING TO: " + exchange.getRequest().getURI());

        if (isRoutePublic(path)) {
            return chain.filter(exchange);
        }
        System.out.println("GATEWAY REACHED");
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        Claims claims;
        try {
            claims = jwtGatewayValidator.parseClaims(token);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        if (claims.getExpiration().before(new Date()) || claims.getExpiration()  == null)
            return unauthorized(exchange);
        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);
        String username = claims.getSubject();
        if (userId == null || role == null) {
            return unauthorized(exchange);
        }
        String id = userId.toString();
        if (path.startsWith("/api/auth/admin") && !"ADMIN".equals(role))
            return forbidden(exchange);
        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .headers(h->{
                    h.remove("X-User-Id");
                    h.remove("X-Username");
                    h.remove("X-Role");
                })
                .header("X-User-Id", id)
                .header("X-Username", username)
                .header("X-Role", role)
                .build();

        return chain.filter(exchange.mutate()
                .request(modifiedRequest)
                .build());
    }

    private boolean isRoutePublic(String path){
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
