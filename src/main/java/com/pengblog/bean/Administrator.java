package com.pengblog.bean;

import java.util.Date;

public class Administrator {
	
	private Integer administrator_id;
	private String administrator_username;
	private String administrator_password;
	private Date administrator_saltDate;
	
	
	
	public Date getAdministrator_saltDate() {
		return administrator_saltDate;
	}

	public void setAdministrator_saltDate(Date administrator_saltDate) {
		this.administrator_saltDate = administrator_saltDate;
	}

	public Integer getAdministrator_id() {
		return administrator_id;
	}

	public void setAdministrator_id(Integer administrator_id) {
		this.administrator_id = administrator_id;
	}



	public String getAdministrator_username() {
		return administrator_username;
	}

	public void setAdministrator_username(String administrator_username) {
		this.administrator_username = administrator_username;
	}

	public String getAdministrator_password() {
		return administrator_password;
	}

	public void setAdministrator_password(String administrator_password) {
		this.administrator_password = administrator_password;
	}



	public Administrator(Integer administrator_id, String administrator_username, String administrator_password,
			Date administrator_saltDate) {
		super();
		this.administrator_id = administrator_id;
		this.administrator_username = administrator_username;
		this.administrator_password = administrator_password;
		this.administrator_saltDate = administrator_saltDate;
	}

	public Administrator() {
		super();
	}
	
}
