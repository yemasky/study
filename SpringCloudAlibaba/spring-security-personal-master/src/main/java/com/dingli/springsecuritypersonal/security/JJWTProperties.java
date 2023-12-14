package com.dingli.springsecuritypersonal.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jjwt.security")
public class JJWTProperties {
    private String secret;

    private String base64Secret;

    private long tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe;
}
