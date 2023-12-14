# SpringCloud（第 015 篇）电影Ribbon微服务集成Hystrix增加隔离策略控制线程数或请求数来达到熔断降级的作用
-

## 一、大致介绍

``` 
1、本章节介绍关于Hystrix的2种隔离方式（Thread Pool 和 Semaphores）；
2、ThreadPool：这是比较常用的隔离策略，即根据配置把不同的命令分配到不同的线程池中，该策略的优点是隔离性好，并且可以配置断路，某个依赖被设置断路之后，系统不会再尝试新起线程运行它，而是直接提示失败，或返回fallback值；缺点是新起线程执行命令，在执行的时候必然涉及到上下文的切换，这会造成一定的性能消耗，但是Netflix做过实验，这种消耗对比其带来的价值是完全可以接受的。
3、Semaphores：开发者可以限制系统对某一个依赖的最高并发数。这个基本上就是一个限流的策略。每次调用依赖时都会检查一下是否到达信号量的限制值，如达到，则拒绝。该隔离策略的优点不新起线程执行命令，减少上下文切换，缺点是无法配置断路，每次都一定会去尝试获取信号量。 
4、而本章节主要简单的介绍下如何使用 Semaphores 来达到控制隔离；
```

## 二、实现步骤

### 2.1 添加 maven 引用包
``` 
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

	<artifactId>springms-consumer-movie-ribbon-with-hystrix-propagation</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
	
    <parent>
        <groupId>com.springms.cloud</groupId>
        <artifactId>springms-spring-cloud</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
	
	<dependencies>
        <!-- web模块 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

        <!-- 客户端发现模块 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

        <!-- Hystrix 断路器模块 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>
    </dependencies>

</project>
```


### 2.2 添加应用配置文件（springms-consumer-movie-ribbon-with-hystrix-propagation\src\main\resources\application.yml）
``` 
spring:
  application:
    name: springms-consumer-movie-ribbon-with-hystrix-propagation
server:
  port: 8100
#做负载均衡的时候，不需要这个动态配置的地址
#user:
#  userServicePath: http://localhost:7900/simple/
eureka:
  client:
#    healthcheck:
#      enabled: true
    serviceUrl:
      defaultZone: http://admin:admin@localhost:8761/eureka
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${spring.cloud.client.ipAddress}:${spring.application.instance_id:${server.port}}


# 解决第一次请求报超时异常的方案，因为 hystrix 的默认超时时间是 1 秒，因此请求超过该时间后，就会出现页面超时显示 ：
#
# 这里就介绍大概三种方式来解决超时的问题，解决方案如下：
#
# 第一种方式：将 hystrix 的超时时间设置成 5000 毫秒
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 5000
#
# 或者：
# 第二种方式：将 hystrix 的超时时间直接禁用掉，这样就没有超时的一说了，因为永远也不会超时了
# hystrix.command.default.execution.timeout.enabled: false
#
# 或者：
# 第三种方式：索性禁用feign的hystrix支持
# feign.hystrix.enabled: false ## 索性禁用feign的hystrix支持

# 超时的issue：https://github.com/spring-cloud/spring-cloud-netflix/issues/768
# 超时的解决方案： http://stackoverflow.com/questions/27375557/hystrix-command-fails-with-timed-out-and-no-fallback-available
# hystrix配置： https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.thread.timeoutInMilliseconds
```

### 2.3 添加实体用户类User（springms-consumer-movie-ribbon-with-hystrix-propagation\src\main\java\com\springms\cloud\entity\User.java）
``` 
package com.springms.cloud.entity;

import java.math.BigDecimal;

public class User {

    private Long id;

    private String username;

    private String name;

    private Short age;

    private BigDecimal balance;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getAge() {
        return this.age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
```




### 2.4 添加电影Web访问层Controller（springms-consumer-movie-ribbon-with-hystrix-propagation\src\main\java\com\springms\cloud\controller\MovieRibbonHystrixPropagationController.java）
``` 
package com.springms.cloud.controller;

import com.springms.cloud.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieRibbonHystrixPropagationController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/movie/{id}")
    @HystrixCommand(fallbackMethod = "findByIdFallback", commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public User findById(@PathVariable Long id) {
        // http://localhost:7900/simple/
        // VIP：virtual IP
        // HAProxy Heartbeat

        System.out.println("======================== findById " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() + " - " + Thread.currentThread().getName());

        return this.restTemplate.getForObject("http://springms-provider-user/simple/" + id, User.class);
    }

    public User findByIdFallback(Long id) {
        System.out.println("======================== findByIdFallback " + Thread.currentThread().getThreadGroup() + " - " + Thread.currentThread().getId() + " - " + Thread.currentThread().getName());

        User user = new User();
        user.setId(0L);
        return user;
    }
}
```


### 2.5 添加电影微服务启动类（springms-consumer-movie-ribbon-with-hystrix-propagation\src\main\java\com\springms\cloud\MsConsumerMovieRibbonHystrixPropagationApplication.java）
``` 
package com.springms.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 电影Ribbon微服务集成Hystrix增加隔离策略控制线程数或请求数来达到熔断降级的作用。
 *
 * 传播安全上下文或使用，通过增加 HystrixCommand 的 commandProperties 属性，来增加相关的配置来达到执行隔离策略，控制线程数或者控制并发请求数来达到熔断降级的作用。
 *
 * Hystrix 断路器实现失败快速响应，达到熔断效果；
 *
 * 注解 EnableCircuitBreaker 表明需要集成断路器模块；
 *
 * 如果你想把本地线程上下文传播到@HystrixCommand，默认的声明将不可用因为它是在一个线程池中被启动的。你可以选择让Hystrix使用同一个线程，通过一些配置，或直接写在注解上，通过使用isolation strategy属性；
 *
 * @author hmilyylimh
 *
 * @version 0.0.1
 *
 * @date 2017/9/21
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class MsConsumerMovieRibbonHystrixPropagationApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MsConsumerMovieRibbonHystrixPropagationApplication.class, args);
		System.out.println("【【【【【【 电影微服务-Hystrix安全传播上下文 】】】】】】已启动.");
	}
}
```


## 三、测试

``` 
/****************************************************************************************
 一、电影 Ribbon 微服务集成 Hystrix 断路器实现失败快速响应，达到熔断效果：

 1、注解：EnableCircuitBreaker、HystrixCommand 的编写；
 2、启动 springms-provider-user 模块服务，启动1个端口；
 3、启动 springms-consumer-movie-ribbon-with-hystrix-propagation 模块服务；
 4、在浏览器输入地址http://localhost:8100/movie/1，然后页面的信息是否有打印出来用户的Id=0的情况，正常情况下是没有用户Id=0的情况信息打印的；

 5、杀死 springms-provider-user 模块服务，停止提供服务；
 6、在浏览器输入地址http://localhost:8100/movie/1，然后页面的信息是否有打印出来用户的Id=0的情况，等了1秒中后有用户Id=0的情况信息打印出来；
 7、然后看看控制台打印的日志：
	 ======================== findById java.lang.ThreadGroup[name=main,maxpri=10] - 56 - http-nio-8100-exec-8
	 ======================== findByIdFallback java.lang.ThreadGroup[name=main,maxpri=10] - 56 - http-nio-8100-exec-8
	 ======================== findById java.lang.ThreadGroup[name=main,maxpri=10] - 57 - http-nio-8100-exec-9
	 ======================== findByIdFallback java.lang.ThreadGroup[name=main,maxpri=10] - 57 - http-nio-8100-exec-9
	 ======================== findById java.lang.ThreadGroup[name=main,maxpri=10] - 35 - http-nio-8100-exec-1
	 ======================== findByIdFallback java.lang.ThreadGroup[name=main,maxpri=10] - 35 - http-nio-8100-exec-1

 总结一：使用 SEMAPHORE 信号量的时候，虽然不能配置断路器功能，但是通过控制请求数来达到一个限流的作用；

 8、等一会儿在启动 springms-provider-user 模块服务，启动1个端口；
 9、在浏览器输入地址http://localhost:8070/movie/1，然后页面的信息又有Id!=0的用户信息打印出来；

 总结二：当远端微服务宕机或者不可用时，Hystrix已经达到快速响应快速失败，起到了熔断机制的效果。
 ****************************************************************************************/







/****************************************************************************************
 找出一些相关的配置信息仅供参考：

 Execution相关的属性的配置：
 hystrix.command.default.execution.isolation.strategy 隔离策略，默认是Thread, 可选Thread｜Semaphore
     thread 通过线程数量来限制并发请求数，可以提供额外的保护，但有一定的延迟。一般用于网络调用
     semaphore 通过semaphore count来限制并发请求数，适用于无网络的高并发请求
 hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds 命令执行超时时间，默认1000ms
 hystrix.command.default.execution.timeout.enabled 执行是否启用超时，默认启用true
 hystrix.command.default.execution.isolation.thread.interruptOnTimeout 发生超时是是否中断，默认true
 hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests 最大并发请求数，默认10，该参数当使用ExecutionIsolationStrategy.SEMAPHORE策略时才有效。如果达到最大并发请求数，请求会被拒绝。理论上选择semaphore size的原则和选择thread size一致，但选用semaphore时每次执行的单元要比较小且执行速度快（ms级别），否则的话应该用thread。
 semaphore应该占整个容器（tomcat）的线程池的一小部分。

 Fallback相关的属性：
 这些参数可以应用于Hystrix的THREAD和SEMAPHORE策略
 hystrix.command.default.fallback.isolation.semaphore.maxConcurrentRequests 如果并发数达到该设置值，请求会被拒绝和抛出异常并且fallback不会被调用。默认10
 hystrix.command.default.fallback.enabled 当执行失败或者请求被拒绝，是否会尝试调用hystrixCommand.getFallback() 。默认true
 Circuit Breaker相关的属性
 hystrix.command.default.circuitBreaker.enabled 用来跟踪circuit的健康性，如果未达标则让request短路。默认true
 hystrix.command.default.circuitBreaker.requestVolumeThreshold 一个rolling window内最小的请求数。如果设为20，那么当一个rolling window的时间内（比如说1个rolling window是10秒）收到19个请求，即使19个请求都失败，也不会触发circuit break。默认20
 hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds 触发短路的时间值，当该值设为5000时，则当触发circuit break后的5000毫秒内都会拒绝request，也就是5000毫秒后才会关闭circuit。默认5000
 hystrix.command.default.circuitBreaker.errorThresholdPercentage错误比率阀值，如果错误率>=该值，circuit会被打开，并短路所有请求触发fallback。默认50
 hystrix.command.default.circuitBreaker.forceOpen 强制打开熔断器，如果打开这个开关，那么拒绝所有request，默认false
 hystrix.command.default.circuitBreaker.forceClosed 强制关闭熔断器 如果这个开关打开，circuit将一直关闭且忽略circuitBreaker.errorThresholdPercentage

 Metrics相关参数：
 hystrix.command.default.metrics.rollingStats.timeInMilliseconds 设置统计的时间窗口值的，毫秒值，circuit break 的打开会根据1个rolling window的统计来计算。若rolling window被设为10000毫秒，则rolling window会被分成n个buckets，每个bucket包含success，failure，timeout，rejection的次数的统计信息。默认10000
 hystrix.command.default.metrics.rollingStats.numBuckets 设置一个rolling window被划分的数量，若numBuckets＝10，rolling window＝10000，那么一个bucket的时间即1秒。必须符合rolling window % numberBuckets == 0。默认10
 hystrix.command.default.metrics.rollingPercentile.enabled 执行时是否enable指标的计算和跟踪，默认true
 hystrix.command.default.metrics.rollingPercentile.timeInMilliseconds 设置rolling percentile window的时间，默认60000
 hystrix.command.default.metrics.rollingPercentile.numBuckets 设置rolling percentile window的numberBuckets。逻辑同上。默认6
 hystrix.command.default.metrics.rollingPercentile.bucketSize 如果bucket size＝100，window＝10s，若这10s里有500次执行，只有最后100次执行会被统计到bucket里去。增加该值会增加内存开销以及排序的开销。默认100
 hystrix.command.default.metrics.healthSnapshot.intervalInMilliseconds 记录health 快照（用来统计成功和错误绿）的间隔，默认500ms

 Request Context 相关参数：
 hystrix.command.default.requestCache.enabled 默认true，需要重载getCacheKey()，返回null时不缓存
 hystrix.command.default.requestLog.enabled 记录日志到HystrixRequestLog，默认true

 Collapser Properties 相关参数：
 hystrix.collapser.default.maxRequestsInBatch 单次批处理的最大请求数，达到该数量触发批处理，默认Integer.MAX_VALUE
 hystrix.collapser.default.timerDelayInMilliseconds 触发批处理的延迟，也可以为创建批处理的时间＋该值，默认10
 hystrix.collapser.default.requestCache.enabled 是否对HystrixCollapser.execute() and HystrixCollapser.queue()的cache，默认true

 ThreadPool 相关参数：
 线程数默认值10适用于大部分情况（有时可以设置得更小），如果需要设置得更大，那有个基本得公式可以follow：
 requests per second at peak when healthy × 99th percentile latency in seconds + some breathing room
 每秒最大支撑的请求数 (99%平均响应时间 + 缓存值)
 比如：每秒能处理1000个请求，99%的请求响应时间是60ms，那么公式是：
 1000 （0.060+0.012）

 ****************************************************************************************/
```


## 四、下载地址

[https://gitee.com/ylimhhmily/SpringCloudTutorial.git](https://gitee.com/ylimhhmily/SpringCloudTutorial.git)

SpringCloudTutorial交流QQ群: 235322432

SpringCloudTutorial交流微信群: [微信沟通群二维码图片链接](https://gitee.com/ylimhhmily/SpringCloudTutorial/blob/master/doc/qrcode/SpringCloudWeixinQrcode.png)

欢迎关注，您的肯定是对我最大的支持!!!






























