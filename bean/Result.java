package com.zhihui.dontStarve.bean;

import java.util.Map;

public class Result {
	Boolean success;
	String message;
	Map<String, Object> data;
	
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Result(Boolean success, String message, Map<String, Object> data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message
				+ ", data=" + data + "]";
	}
	
	
}
