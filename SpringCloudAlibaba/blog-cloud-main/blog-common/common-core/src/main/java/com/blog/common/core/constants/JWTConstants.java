package com.blog.common.core.constants;

/**
 * @path：com.blog.common.core.constants.JWTConstants.java
 * @className：JWTConstants.java
 * @description：JWT常量
 * @author：tanyp
 * @dateTime：2020/11/9 15:31
 * @editNote：
 */
public class JWTConstants {

    public static final byte[] SECRET = "52d907a4b404af790cf2cf488acc4836".getBytes();

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 角色开头
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * 过期时间2小时
     */
    public static final long EXPIRE_TIME = 1000 * 60 * 60 * 2;

    /**
     * Token已过期
     */
    public static final String TOKEN_EXPIRE = "Token已过期";

    /**
     * Token无效
     */
    public static final String TOKEN_INVALID = "Token无效";

}