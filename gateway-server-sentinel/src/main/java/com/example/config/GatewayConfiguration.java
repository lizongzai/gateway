package com.example.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 限流规则配置类
 */
@Configuration
public class GatewayConfiguration {

  private final List<ViewResolver> viewResolvers;
  private final ServerCodecConfigurer serverCodecConfigurer;

  /**
   * 构造器
   *
   * @param viewResolversProvider
   * @param serverCodecConfigurer
   */
  public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
      ServerCodecConfigurer serverCodecConfigurer) {
    this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
    this.serverCodecConfigurer = serverCodecConfigurer;
  }

  /**
   * 限流异常处理器
   *
   * @return
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
    // Register the block exception handler for Spring Cloud Gateway.
    return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
  }

  /**
   * 限流过滤器
   *
   * @return
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public GlobalFilter sentinelGatewayFilter() {
    return new SentinelGatewayFilter();
  }

  /**
   * Spring 容器初始化的时候执行该方法
   */
  @PostConstruct
  public void doInit() {
    // 加载网关限流规则
    initGatewayRules();
    // 加载自定义限流异常处理器
    initBlockHandler();
  }

  /**
   * 网关限流规则
   */
  private void initGatewayRules() {
    Set<GatewayFlowRule> rules = new HashSet<>();
        /*
            resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称
            count：限流阈值
            intervalSec：统计时间窗口，单位是秒，默认是 1 秒
         */
    rules.add(new GatewayFlowRule("order-service")
        .setCount(3) // 限流阈值
        .setIntervalSec(60)); // 统计时间窗口，单位是秒，默认是 1 秒

    rules.add(new GatewayFlowRule("product-service")
        .setCount(3) // 限流阈值
        .setIntervalSec(60)); // 统计时间窗口，单位是秒，默认是 1 秒

    // 加载网关限流规则
    GatewayRuleManager.loadRules(rules);
  }

  /**
   * 自定义限流异常处理器
   */
  private void initBlockHandler() {
    BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
      @Override
      public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange,
          Throwable throwable) {
        Map<String, String> result = new HashMap<>();
        result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
        result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        //采用限流分组方式
        //result.put("route", "order-service");
        result.put("route", "product-service");
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(result));
      }
    };

    // 加载自定义限流异常处理器
    GatewayCallbackManager.setBlockHandler(blockRequestHandler);
  }

}
