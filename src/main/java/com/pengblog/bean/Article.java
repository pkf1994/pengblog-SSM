package com.pengblog.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peng Kaifan
 *
 */
public class Article implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer article_id;
	
	private String article_title;
	
	private String article_author;
	
	private String article_content;
	
	private String article_summary;
	
	private Date article_releaseTime;
	
	private Date article_deleteTime;
	
	private String article_label;
	
	private String article_type;
	
	private String article_previewImageUrl;
	
	private String article_titleImageUrl;
	
	private int article_countOfAllComment;
	
	private boolean isDeleted;

	public int getArticle_countOfAllComment() {
		return article_countOfAllComment;
	}

	public void setArticle_countOfAllComment(int article_countOfAllComment) {
		this.article_countOfAllComment = article_countOfAllComment;
	}

	public Date getArticle_deleteTime() {
		return article_deleteTime;
	}

	public void setArticle_deleteTime(Date article_deleteTime) {
		this.article_deleteTime = article_deleteTime;
	}

	/**
	 * @return the article_id
	 */
	public Integer getArticle_id() {
		return article_id;
	}

	/**
	 * @param article_id the article_id to set
	 */
	public void setArticle_id(Integer article_id) {
		this.article_id = article_id;
	}

	/**
	 * @return the article_title
	 */
	public String getArticle_title() {
		return article_title;
	}

	/**
	 * @param article_title the article_title to set
	 */
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	/**
	 * @return the article_author
	 */
	public String getArticle_author() {
		return article_author;
	}

	/**
	 * @param article_author the article_author to set
	 */
	public void setArticle_author(String article_author) {
		this.article_author = article_author;
	}

	/**
	 * @return the article_content
	 */
	public String getArticle_content() {
		return article_content;
	}

	/**
	 * @param article_content the article_content to set
	 */
	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

	/**
	 * @return the article_summary
	 */
	public String getArticle_summary() {
		return article_summary;
	}

	/**
	 * @param article_summary the article_summary to set
	 */
	public void setArticle_summary(String article_summary) {
		this.article_summary = article_summary;
	}

	

	/**
	 * @return the article_releaseTime
	 */
	public Date getArticle_releaseTime() {
		return article_releaseTime;
	}

	/**
	 * @param article_releaseTime the article_releaseTime to set
	 */
	public void setArticle_releaseTime(Date article_releaseTime) {
		this.article_releaseTime = article_releaseTime;
	}

	/**
	 * @return the article_label
	 */
	public String getArticle_label() {
		return article_label;
	}

	/**
	 * @param article_label the article_label to set
	 */
	public void setArticle_label(String article_label) {
		this.article_label = article_label;
	}

	
	public String getArticle_type() {
		return article_type;
	}

	public void setArticle_type(String article_type) {
		this.article_type = article_type;
	}

	

	public String getArticle_previewImageUrl() {
		return article_previewImageUrl;
	}

	public void setArticle_previewImageUrl(String article_previewImageUrl) {
		this.article_previewImageUrl = article_previewImageUrl;
	}

	public String getArticle_titleImageUrl() {
		return article_titleImageUrl;
	}

	public void setArticle_titleImageUrl(String article_titleImageUrl) {
		this.article_titleImageUrl = article_titleImageUrl;
	}
	 
	
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

	public Article(Integer article_id, String article_title, String article_author, String article_content,
			String article_summary, Date article_releaseTime, Date article_deleteTime, String article_label,
			String article_type, String article_previewImageUrl, String article_titleImageUrl,
			int article_countOfAllComment, boolean isDeleted) {
		super();
		this.article_id = article_id;
		this.article_title = article_title;
		this.article_author = article_author;
		this.article_content = article_content;
		this.article_summary = article_summary;
		this.article_releaseTime = article_releaseTime;
		this.article_deleteTime = article_deleteTime;
		this.article_label = article_label;
		this.article_type = article_type;
		this.article_previewImageUrl = article_previewImageUrl;
		this.article_titleImageUrl = article_titleImageUrl;
		this.article_countOfAllComment = article_countOfAllComment;
		this.isDeleted = isDeleted;
	}

	public Article() {
		super();
	}
	
	
}
