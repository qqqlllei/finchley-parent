//package com.gateway.server.filter;
//
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by 李雷 on 2019/1/24.
// */
//@Component
//public class GrayFilter implements GlobalFilter,Ordered{
//
//
//    public static final String META_DATA_KEY_VERSION = "version";
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        String version = exchange.getRequest().getQueryParams().getFirst(META_DATA_KEY_VERSION);
//        if(StringUtils.isBlank(version)){
//            version = exchange.getRequest().getHeaders().getFirst(META_DATA_KEY_VERSION);
//        }
//        Map<String,String> threalLocalItem = new HashMap<>();
//        threalLocalItem.put(META_DATA_KEY_VERSION,version);
//        ThreadLocalContext.initHystrixRequestContext(version);
//
//
//        ServerHttpRequest host = exchange.getRequest().mutate().header(META_DATA_KEY_VERSION, version).build();
//        return chain.filter(exchange.mutate().request(host).build()).then(Mono.fromRunnable( () -> ThreadLocalContext.shutdownHystrixRequestContext()));
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}
