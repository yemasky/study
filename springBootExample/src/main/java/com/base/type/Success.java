package com.base.type;

import java.sql.Timestamp;

public class Success {
	private boolean success = true;
	private String code = "100001";
	private String module;
	private String message = "请求数据成功";
	private Object data = null;
	private Timestamp date = null;
	
	public Success() {
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
	}
	
	public Success(ErrorCode error) {
		this.code = error.getError_code();
		this.message = error.getDescribe();
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
	}
	
	public void setCode(ErrorCode error) {
		this.code = error.getError_code();
		this.message = error.getDescribe();
		long thisDatetime = System.currentTimeMillis();
		this.date = new Timestamp(thisDatetime);
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
