package com.pengblog.serviceInterface;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pengblog.bean.Article;

/**
 * @author Administrator
 *	article���ҵ��ӿ�
 */
public interface IarticleService {

	/**
	 * @param currentPage
	 * @param pageScale
	 * @return
	 */
	Article[] getArticleSummaryList(int startIndex, int pageScale, int deletedStatus);

	/**
	 * @param pageScale
	 * @return
	 */
	int getMaxPage(int pageScale, int deletedStatus);

	/**
	 * @param article_id
	 * @return
	 */
	Article getArticleById(int article_id);

	Article constructArticle(Map<String, String> articleData);
	
	int saveArticle(Article article);

	Article handleImageUrl(Article article);

	void deleteArticleById(int article_id);
	
	void destroyArticleById(int article_id);

	void updateArticle(Article handledArticle);

	Map<Integer, Object> getArticleFiling(int deletedStatus);

	List<Map<String, Integer>> getArticleLabelList(int deletedStatus);

	Article[] getArticleItemListByLimitIndexAndSearchWords(int startIndex, int pageScale, String[] searchWords, int deletedStatus);

	int getMaxPageBySearchWords(int pageScale, String[] searchWords, int deletedStatus);

	int getCountOfArticleBySearchWords(String[] searchWords, int deletedStatus);

	Article[] getArticleItemListByLimitIndexAndYearAndMonth(int startIndex, int pageScale, String selectedYear,
			String selectedMonth, int deletedStatus);

	int getMaxPageByYearAndMonth(int pageScale, String selectedYear, String selectedMonth, int deletedStatus);

	int getCountOfArticleByYearAndMonth(String selectedYear, String selectedMonth, int deletedStatus);

	Article[] getArticleItemListByLimitIndexAndLabel(int startIndex, int pageScale, String article_label, int deletedStatus);

	int getMaxPageByLabel(int pageScale, String article_label, int deletedStatus);

	int getCountOfArticleByLabel(String article_label, int deletedStatus);

	Article handlePreviewImage(Article handledArticle);

	Article getDraft();

	int getCountOfDeletedArticleList();

	Article[] getDeletedArticleList(int startIndex, int pageScale);

	int getMaxPageOfDeletedArticle(int pageScale);

	void recoverArticle(int article_id);

	void destroyAllArticleDeleted();

	int getCountOfArticle(int deletedStatus);
	
	int generateDeletedStatus(HttpServletRequest request);

}
