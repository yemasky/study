package com.seata.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient
@MapperScan(value = "com.seata.storage.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SeataStorageSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataStorageSamplesApplication.class, args);
    }

}
