package com.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @path：com.blog.config.ExclusionUrlConfig.java
 * @className：ExclusionUrlConfig.java
 * @description：白名单配置
 * @author：tanyp
 * @dateTime：2020/11/19 14:01 
 * @editNote：
 */
@Data
@Component
@ConfigurationProperties(prefix = "exclusion")
public class ExclusionUrlConfig {

    private List<String> url;

}
