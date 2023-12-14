# Blog Cloud


Spring Cloud Alibaba 致力于提供微服务开发的一站式解决方案。此项目包含开发分布式应用微服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。

依托 Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud 应用接入阿里微服务解决方案，通过阿里中间件来迅速搭建分布式应用系统。


## 版本介绍

- java.version 1.8
- spring-boot.version：2.3.2.RELEASE
- spring-cloud.version：Hoxton.SR8
- com.alibaba.cloud.version：2.2.3.RELEASE
- nimbus-jose-jwt.version：9.1.1
- lombok.version：1.18.12
- fastjson.version：1.2.62
- druid.version：1.1.10
- mybatis-plus.version：3.2.0
- mysql.version：5.1.38

>版本参考地址：[https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

---

## 软件架构

### 一、组件

组件名称|版本号|描述
:----|:----|:----
 Nacos Discovery |----| 注册中心 
 Nacos Config |----| 配置中心 
 Dubbo Spring Cloud |----| 服务通信 
 Spring Cloud Gateway |----| 网关 
 Spring Cloud Security |----| 安全认证 
 Spring Cloud Hystrix |----| 服务熔断 
 Spring Cloud Sleuth + Zipkin |----| 调用链监控 
 Spring Data Redis |----| NoSQL数据库 
 ... | ... | ... 


### 二、模块

模块名称|描述|备注
:----|:----|:----
 blog-gateway | 网关 | -- 
 blog-auth | 认证中心 | -- 
 blog-common | 公共组件 | -- 
 blog-contract | Dubbo接口暴露 | -- 
 blog-user | 用户权限管理 | -- 
 ... | ... | ... |

---

## 功能介绍

### 一、Nacos注册中心、配置中心

> 以网关配置为例（blog-gateway）
> 官网文档：
>> 注册中心：[https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-docs/src/main/asciidoc-zh/nacos-discovery.adoc](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-docs/src/main/asciidoc-zh/nacos-discovery.adoc)

>> 配置中心：[https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-docs/src/main/asciidoc-zh/nacos-config.adoc](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-docs/src/main/asciidoc-zh/nacos-config.adoc)

Nacos Server 启动后，进入 http://localhost:8848/nacos/index.html 查看控制台(默认账号名/密码为 nacos/nacos):

#### 1、添加依赖

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

#### 2、添加配置文件(bootstrap.yml)

```
spring:
  application:
    name: blog-gateway
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848 # Nacos服务地址
        file-extension: yaml # 配置格式
#        shared-dataids: all-gateway.yaml # 全局引入的配置
#        refresh-dataids: all-gateway.yaml # 实现动态配置刷新
        namespace: 57cace63-8a7e-4051-825b-ba5ff39f9911 # 命名空间
  profiles:
    active: dev # 环境配置
```

#### 3、创建配置文件

在配置管理 》 配置列表 中添加 `blog-gateway-dev.yaml` 文件。

> 命名规则： 服务名称（`blog-gateway`） + 环境名称（`dev、test`） + 后缀（`.yaml、.properties`）

#### 4、注意事项

- 命名规则必须一致，否则不生效；
- 配置格式必须对应，否则不生效；
- 如使用命名空间，则命名空间必须对应，否则不生效；
- 如使用全局配置，必须配置（`shared-dataids、refresh-dataids`）全局引入的配置和实现动态配置刷新。

#### 5、Nacos基本概念

5.1、命名空间（namespace）

命名空间可用于进行不同环境的配置隔离，一般一个环境划分一个命名空间。

5.2、配置分组（group）

配置分组用于讲不同的服务可以归类到同一分组，一般讲一个项目的配置分到一个组中。

5.3、配置集（dateId）

在系统中，一个配置文件就是一个配置集，一般微服务的配置就是一个配置集。

### 二、熔断&限流

#### 1、添加依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

#### 2、添加配置

```
# 配置路由规则
routes:
    # 采用自定义路由ID
    - id: blog-auth
      # 采用 LoadBalanceClient 方式请求，以 lb:// 开头，后面的是注册在 Nacos 上的服务名
      uri: lb://blog-auth
      predicates:
        - Path=/auth/**
      filters:
        - StripPrefix=1
        - name: Hystrix  # 熔断
          args:
            name: fallbackcmd
            # fallback 时调用的方法 http://localhost:8080/fallback
            fallbackUri: forward:/fallback
        - name: RequestRateLimiter  # 限流
          args:
            key-resolver: '#{@uriKeyResolver}'  # 限流过滤器的 Bean 名称
            redis-rate-limiter.replenishRate: 1  # 希望允许用户每秒处理多少个请求
            redis-rate-limiter.burstCapacity: 3  # 用户允许在一秒钟内完成的最大请求数
```

#### 3、熔断实现

3.1、Hystrix服务降级处理

```
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        originalUris.ifPresent(originalUri -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUri));

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "您的操作太频繁，请稍后重试");
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(response));
    }

}
```

3.2、服务降级处理(fallback)

```
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

    private final HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.path("/fallback")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
    }
}
```

#### 4、限流实现

```
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
```

> 限流过滤器的Bean名称（`key-resolver: '#{@uriKeyResolver}'`）保持一致 `UriKeyResolver.java`

 
### 三、跨域配置

```
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOrigin("*");
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        // 允许Get的请求方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
```

### 四、链路追踪

> 使用 Spring Cloud Sleuth + Zipkin 作为微服务架构分布式服务跟踪。

#### 1、SpringCloud Sleuth 

为服务之间调用提供链路追踪。通过 Sleuth 可以很清楚的了解到一个服务请求经过了哪些服务，每个服务处理花费了多长。从而让我们可以很方便的理清各微服务间的调用关系。此外 Sleuth 可以帮助我们：

- 耗时分析：通过 Sleuth 可以很方便的了解到每个采样请求的耗时，从而分析出哪些服务调用比较耗时；
- 可视化错误：对于程序未捕捉的异常，可以通过集成 Zipkin 服务界面上看到；
- 链路优化：对于调用比较频繁的服务，可以针对这些服务实施一些优化措施。

> Spring Cloud Sleuth 可以结合 Zipkin，将信息发送到 Zipkin，利用 Zipkin 的存储来存储信息，利用 Zipkin UI 来展示数据。

#### 2、Zipkin

Zipkin 是 Twitter 的开源分布式跟踪系统，它基于 Google Dapper 实现，它致力于收集服务的时序数据，以解决微服务架构中的延迟问题，包括数据的收集、存储、查找和展现。

2.1、下载Zipkin Server

[https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/](https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/)

2.2、启动Zipkin Server

下载完成之后，在下载的jar所在目录，执行 `java -jar *****.jar` 命令即可启动Zipkin Server

2.3、访问

[http://localhost:9411](http://localhost:9411) 即可看到Zipkin Server的首页。

#### 3、添加配置

```
spring:
  zipkin:
    base-url: http://localhost:9411/
  sleuth:
    sampler:
      # 抽样率，默认是0.1（90%的数据会被丢弃）
      # 这边为了测试方便，将其设置为1.0，即所有的数据都会上报给zipkin
      probability: 1.0
```

#### 4、Zipkin数据持久化

当前搭建Zipkin是基于内存的，如果Zipkin发生重启的话，数据就会丢失，这种方式是不适用于生产的，所以我们需要实现数据持久化。
Zipkin给出三种数据持久化方法：

- MySQL：存在性能问题，不建议使用
- Elasticsearch
- Cassandra

相关的官方文档：[https://github.com/openzipkin/zipkin#storage-component](https://github.com/openzipkin/zipkin#storage-component) 介绍Elasticsearch实现Zipkin数据。

#### 欢迎关注

![个人公众号](https://images.gitee.com/uploads/images/2020/0425/154219_3cff657a_1739235.jpeg "欢迎关注个人公众号")
