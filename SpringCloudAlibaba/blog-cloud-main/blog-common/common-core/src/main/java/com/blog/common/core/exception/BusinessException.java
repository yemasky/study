package com.blog.common.core.exception;


/**
 * @path：com.blog.common.core.exception.BusinessException.java
 * @className：BusinessException.java
 * @description：业务异常处理
 * @author：tanyp
 * @dateTime：2020/11/9 16:48
 * @editNote：
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 异常码
     */
    private int code = 500;

    /**
     * 异常描述
     */
    private String message;

    /**
     * 构造异常
     *
     * @param code    异常码
     * @param message 异常描述
     */
    public BusinessException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造异常
     *
     * @param code    异常码
     * @param message 异常描述
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }

}
