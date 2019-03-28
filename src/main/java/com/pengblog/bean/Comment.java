package com.pengblog.bean;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer comment_id;
	
	private Visitor comment_author;
	
	private Comment comment_referComment;
	
	private String comment_content;
	
	private Date comment_releaseTime;
	
	private Article comment_hostArticle;
	
	private Boolean comment_haveSubComment;
	
	private String comment_platform;
	
	private IpObject comment_ip;

	/**
	 * @return the comment_id
	 */
	public Integer getComment_id() {
		return comment_id;
	}

	/**
	 * @param comment_id the comment_id to set
	 */
	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}


	public Visitor getComment_author() {
		return comment_author;
	}

	public void setComment_author(Visitor comment_author) {
		this.comment_author = comment_author;
	}

	/**
	 * @return the comment_content
	 */
	public String getComment_content() {
		return comment_content;
	}

	/**
	 * @param comment_content the comment_content to set
	 */
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}


	


	public Comment getComment_referComment() {
		return comment_referComment;
	}

	public void setComment_referComment(Comment comment_referComment) {
		this.comment_referComment = comment_referComment;
	}

	public Date getComment_releaseTime() {
		return comment_releaseTime;
	}

	public void setComment_releaseTime(Date comment_releaseTime) {
		this.comment_releaseTime = comment_releaseTime;
	}

	public Article getComment_hostArticle() {
		return comment_hostArticle;
	}

	public void setComment_hostArticle(Article comment_hostArticle) {
		this.comment_hostArticle = comment_hostArticle;
	}
	

	public Boolean getComment_haveSubComment() {
		return comment_haveSubComment;
	}

	public void setComment_haveSubComment(Boolean comment_haveSubComment) {
		this.comment_haveSubComment = comment_haveSubComment;
	}

	public String getComment_platform() {
		return comment_platform;
	}

	public void setComment_platform(String comment_platform) {
		this.comment_platform = comment_platform;
	}

	public IpObject getComment_ip() {
		return comment_ip;
	}

	public void setComment_ip(IpObject comment_ip) {
		this.comment_ip = comment_ip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Comment(Integer comment_id, Visitor comment_author, Comment comment_referComment, String comment_content,
			Date comment_releaseTime, Article comment_hostArticle, Boolean comment_haveSubComment,
			String comment_platform, IpObject ipObject) {
		super();
		this.comment_id = comment_id;
		this.comment_author = comment_author;
		this.comment_referComment = comment_referComment;
		this.comment_content = comment_content;
		this.comment_releaseTime = comment_releaseTime;
		this.comment_hostArticle = comment_hostArticle;
		this.comment_haveSubComment = comment_haveSubComment;
		this.comment_platform = comment_platform;
		this.comment_ip = ipObject;
	}

	/**
	 * 
	 */
	public Comment() {
		super();
	}

}
