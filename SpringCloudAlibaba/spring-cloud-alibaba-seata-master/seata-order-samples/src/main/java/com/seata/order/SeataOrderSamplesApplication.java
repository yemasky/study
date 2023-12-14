package com.seata.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(value = "com.seata.order.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SeataOrderSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderSamplesApplication.class, args);
    }

}
