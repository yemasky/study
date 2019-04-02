package com.base.type;

public enum ErrorCode {
	//T TURE 的缩写  F FALSE的缩写
	__T_SUCCESS("100001", "请求数据成功"),
	__T_LOGIN("100002",   "登錄成功"),
	//
	__F_SYS("000000",     "系统错误");
	
	private String error_code;
    private String describe;
	private ErrorCode(String error_code, String describe) {
		this.setError_code(error_code);
        this.setDescribe(describe);
    }
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	
}
