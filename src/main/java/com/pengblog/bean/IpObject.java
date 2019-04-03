package com.pengblog.bean;

import java.io.Serializable;
import java.util.Date;

public class IpObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int ip_id;
	
	private String ip_ip;
	
	private int ip_request_times;
	
	private Boolean ip_isBanned;
	
	private Date ip_banTime;

	
	public int getIp_id() {
		return ip_id;
	}


	public void setIp_id(int ip_id) {
		this.ip_id = ip_id;
	}


	public String getIp_ip() {
		return ip_ip;
	}


	public void setIp_ip(String ip_ip) {
		this.ip_ip = ip_ip;
	}


	public Boolean getIp_isBanned() {
		return ip_isBanned;
	}


	public void setIp_isBanned(Boolean ip_isBanned) {
		this.ip_isBanned = ip_isBanned;
	}


	
	public int getIp_request_times() {
		return ip_request_times;
	}


	public void setIp_request_times(int ip_request_times) {
		this.ip_request_times = ip_request_times;
	}

	

	public Date getIp_banTime() {
		return ip_banTime;
	}


	public void setIp_banTime(Date ip_banTime) {
		this.ip_banTime = ip_banTime;
	}



	public IpObject(int ip_id, String ip_ip, int ip_request_times, Boolean ip_isBanned, Date ip_banTime) {
		super();
		this.ip_id = ip_id;
		this.ip_ip = ip_ip;
		this.ip_request_times = ip_request_times;
		this.ip_isBanned = ip_isBanned;
		this.ip_banTime = ip_banTime;
	}


	public IpObject() {
		super();
	}
	
	
}
