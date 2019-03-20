package com.pengblog.bean;

public class LoginResult {
	
	private Boolean success;
	
	private String message;
	
	private String token;
	
	private long validTimeMillis; 
	
	public long getValidTimeMillis() {
		return validTimeMillis;
	}

	public void setValidTimeMillis(long validTimeMillis) {
		this.validTimeMillis = validTimeMillis;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
	
	
}
