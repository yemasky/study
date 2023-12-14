package com.blog.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @path：com.blog.config.UriKeyResolver.java
 * @className：UriKeyResolver.java
 * @description：限流过滤器
 * @author：tanyp
 * @dateTime：2020/11/25 11:14 
 * @editNote：
 */
@Configuration
public class UriKeyResolver implements KeyResolver {

    /**
     * @methodName：resolve
     * @description：根据请求的 uri 限流
     * @author：tanyp
     * @dateTime：2020/11/25 11:16
     * @Params： [exchange]
     * @Return： reactor.core.publisher.Mono<java.lang.String>
     * @editNote：
     */
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getURI().getPath());
    }

}
