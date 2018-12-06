package com.pengblog.bean;

import java.io.Serializable;

public class Visitor implements Serializable{
	
	private Integer visitor_id;
	
	private String visitor_name;
	
	private String visitor_email;
	
	private String visitor_siteAddress;

	public Integer getVisitor_id() {
		return visitor_id;
	}

	public void setVisitor_id(Integer visitor_id) {
		this.visitor_id = visitor_id;
	}

	public String getVisitor_name() {
		return visitor_name;
	}

	public void setVisitor_name(String visitor_name) {
		this.visitor_name = visitor_name;
	}

	public String getVisitor_email() {
		return visitor_email;
	}

	public void setVisitor_email(String visitor_email) {
		this.visitor_email = visitor_email;
	}

	public String getVisitor_siteAddress() {
		return visitor_siteAddress;
	}

	public void setVisitor_siteAddress(String visitor_siteAddress) {
		this.visitor_siteAddress = visitor_siteAddress;
	}

	public Visitor(Integer visitor_id, String visitor_name, String visitor_email, String visitor_siteAddress) {
		super();
		this.visitor_id = visitor_id;
		this.visitor_name = visitor_name;
		this.visitor_email = visitor_email;
		this.visitor_siteAddress = visitor_siteAddress;
	}

	public Visitor() {
		super();
	}
}
