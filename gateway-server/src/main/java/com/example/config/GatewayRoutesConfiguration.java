package com.example.config;

import com.example.filter.CustomGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述: 网关路由配置类
 *
 * @author lizongzai
 * @date 2023/03/07 10:31
 * @since 1.0.0
 */
//@Configuration
public class GatewayRoutesConfiguration {

  /**
   * 自定义配置路由: 其实与配置文件application.yml文件差不多, 配置文件里面的路由规则基本可以满足日常需求，无需在这里自定义
   *
   * @param builder
   * @return
   */
  @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
    return builder.routes().route(r -> r
        // 断言（判断条件）
        .path("/product/**")
        // 目标 URI，路由到微服务的地址
        .uri("lb://product-service")
        // 注册自定义网关过滤器
        .filters(new CustomGatewayFilter())
        // 路由 ID，唯一
        .id("product-service")).build();
  }
}
