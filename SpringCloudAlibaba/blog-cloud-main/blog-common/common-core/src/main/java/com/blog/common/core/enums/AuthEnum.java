package com.blog.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @path：com.blog.common.core.enums.AuthEnum.java
 * @className：AuthEnum.java
 * @description：安全认证相关
 * @author：tanyp
 * @dateTime：2020/11/24 9:18 
 * @editNote：
 */
@Getter
@AllArgsConstructor
public enum AuthEnum {

    AUTH_NO_TOKEN(401, "Token已过期或有误"),

    AUTH_NO_ACCESS(403, "无访问权限"),

    AUTH_NONEXISTENT(404, "请求路径不存在"),

    ;

    private Integer key;

    private String value;

    /**
     * @methodName：getValue
     * @description：根据key获取value
     * @author：tanyp
     * @dateTime：2020/11/24 9:25
     * @Params： [key]
     * @Return： java.lang.String
     * @editNote：
     */
    public static String getValue(Integer key) {
        for (AuthEnum value : AuthEnum.values()) {
            if (Objects.equals(key, value.getKey())) {
                return value.getValue();
            }
        }
        return null;
    }


}
