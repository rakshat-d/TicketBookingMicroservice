package org.movieBooking.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Order(-2)
public class HttpsRedirectionFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var uri = exchange.getRequest().getURI();
        if (Objects.equals(uri.getScheme(), "http")) {
            var nUri = UriComponentsBuilder.fromUri(uri).scheme("https").build();
            var nRequest = exchange.getRequest().mutate().uri(uri).build();
            exchange = exchange.mutate().request(nRequest).build();
        }
        return chain.filter(exchange);
    }
}
