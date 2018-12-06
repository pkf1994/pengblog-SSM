package com.pengblog.utils;

/**
 * @author Administrator
 *
 */
public enum ArticleFields {
	
	ARTICLE_ID("article_id"),
	ARTICLE_TITLE("article_title"),
	ARTICLE_AUTHOR("article_author"),
	ARTICLE_LABEL("article_label"),
	ARTICLE_CONTENT("article_content"),
	ARTICLE_RELEASETIME("article_releaseTime"),
	ARTICLE_SUMMARY("article_summary");
	
	public final String fieldName;
	
	ArticleFields(String fieldName){
		this.fieldName = fieldName;
	}
	

}
