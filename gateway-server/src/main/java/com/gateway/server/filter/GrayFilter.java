package com.gateway.server.filter;


import com.gray.gateway.util.ThreadLocalContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李雷 on 2019/1/24.
 */
@Component
public class GrayFilter implements GlobalFilter,Ordered{

    private static final String GATEWAY_GRAY_VERSION_NAME="gatewayGrayVersion";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String version = exchange.getRequest().getQueryParams().getFirst(GATEWAY_GRAY_VERSION_NAME);
        if(StringUtils.isBlank(version)){
            version = exchange.getRequest().getHeaders().getFirst(GATEWAY_GRAY_VERSION_NAME);
        }
        Map<String,String> threalLocalItem = new HashMap<>();
        threalLocalItem.put(GATEWAY_GRAY_VERSION_NAME,version);
        ThreadLocalContext.set(threalLocalItem);


        ServerHttpRequest host = exchange.getRequest().mutate().header(GATEWAY_GRAY_VERSION_NAME, version).build();
        return chain.filter(exchange.mutate().request(host).build()).then(Mono.fromRunnable( () -> ThreadLocalContext.clear()));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
