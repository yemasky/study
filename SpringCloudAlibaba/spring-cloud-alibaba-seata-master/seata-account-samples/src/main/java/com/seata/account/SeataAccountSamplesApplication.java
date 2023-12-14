package com.seata.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zxx
 */
@EnableDiscoveryClient
@MapperScan(value = "com.seata.account.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SeataAccountSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataAccountSamplesApplication.class, args);
    }

}
