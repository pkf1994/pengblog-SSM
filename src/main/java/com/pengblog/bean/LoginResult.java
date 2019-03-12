package com.pengblog.bean;

public class LoginResult {
	
	private String loginStatus;
	
	private String message;
	
	private String token;
	
	private int validTimeMillis; 
	
	public int getValidTimeMillis() {
		return validTimeMillis;
	}

	public void setValidTimeMillis(int validTimeMillis) {
		this.validTimeMillis = validTimeMillis;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
