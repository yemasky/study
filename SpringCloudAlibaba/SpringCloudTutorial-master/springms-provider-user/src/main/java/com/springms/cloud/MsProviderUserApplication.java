package com.springms.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 用户服务类（添加服务注册，将用户微服务注册到 EurekaServer 中）。
 *
 * @author hmilyylimh
 *
 * @version 0.0.1
 *
 * @date 2017/9/17
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class MsProviderUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProviderUserApplication.class);
        System.out.println("【【【【【【 用户微服务 】】】】】】已启动.");
    }
}


/****************************************************************************************
 一、用户微服务接口测试：

 1、注解：EnableEurekaClient
 2、启动 springms-discovery-eureka 模块服务，启动1个端口；
 3、启动 springms-provider-user 模块服务，启动1个端口；
 4、在浏览器输入地址http://localhost:7900/simple/1 可以看到信息成功的被打印出来，说明用户微服务正常；

 5、在浏览器输入地址 http://localhost:8761 并输入用户名密码 admin/admin 进入Eureka微服务显示在网页中，说明用户微服务确实注册到了 eureka 服务中；
 6、在浏览器输入地址 http://localhost:8761/eureka/apps/springms-provider-user 可以看到自定义的 <metadata>信息以及用户微服务的相关信息成功的被展示出来了；
 ****************************************************************************************/





/****************************************************************************************
 二、用户微服务接口测试（给 springms-discovery-eureka-ha 模块做测试，测试EurekaClient客户端注册进EurekaServer高可用集群中）：

 1、注解：EnableEurekaClient
 2、修改 defaultZone 的接入地址值如下：
 ###################################################################################
 # 测试二：测试EurekaClient客户端注册进EurekaServer高可用集群中
 defaultZone: http://admin:admin@peer1:8401/eureka,,http://admin:admin@peer2:8402/eureka,,http://admin:admin@peer3:8403/eureka
 ###################################################################################
 3、启动 springms-discovery-eureka-ha 模块服务，启动3个端口；
 4、启动 springms-provider-user 模块服务，启动1个端口；
 5、在浏览器输入地址http://localhost:7900/simple/1 可以看到信息成功的被打印出来，说明用户微服务正常；

 6、在浏览器输入地址 http://localhost:8401 并输入用户名密码 admin/admin 进入Eureka微服务显示在网页中，说明用户微服务确实注册到了 eureka 服务中；
 7、在浏览器输入地址 http://localhost:8401/eureka/apps/springms-provider-user 可以看到自定义的 <metadata>信息以及用户微服务的相关信息成功的被展示出来了；
 8、在浏览器输入地址 http://localhost:8402/eureka/apps/springms-provider-user 可以看到自定义的 <metadata>信息以及用户微服务的相关信息成功的被展示出来了；
 9、在浏览器输入地址 http://localhost:8403/eureka/apps/springms-provider-user 可以看到自定义的 <metadata>信息以及用户微服务的相关信息成功的被展示出来了；
 ****************************************************************************************/


