package com.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 功能描述: 限流规则配置类 1.限流规则配置,不能同时启用任何时候都是唯一 2.pathKeyResolver和parameterKeyResolver只能配置其一
 */
@Configuration
public class KeyResolverConfiguration {

  /**
   * 限流规则
   *
   * @return
   */
  //@Bean
  public KeyResolver pathKeyResolver() {

    return new KeyResolver() {
      @Override
      public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getPath().toString());
      }
    };

    // JDK 1.8
    // return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
  }

  /**
   * 根据参数限流
   *
   * @return
   */
  //@Bean
  public KeyResolver parameterKeyResolver() {
    return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
  }

  /**
   * 根据 IP 限流
   *
   * @return
   */
  @Bean
  public KeyResolver ipKeyResolver() {
    return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
  }

}
