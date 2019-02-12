/**
 * 
 */
package com.pengblog.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.pengblog.bean.Article;

/**
 * @author Administrator
 *
 */

public interface IarticleDao {

	/**
	 * @param startIndex
	 * @param pageScale
	 * @param paramList 
	 * @return
	 */
	Article[] selectArticleListByLimitIndex(@Param("startIndex")int startIndex, 
											@Param("pageScale")int pageScale, 
											@Param("paramList")List<String> paramList,
											@Param("article_type")String article_type);

	
	int selectCountOfArticle(@Param("article_type")String article_type);

	
	Article selectArticleById(int article_id);

	int insertArticle(Article article);

	int selectCountOfDraft();

	void deleteArticleById(int article_id);

	void updateArticle(Article handledArticle);

	int selectCountOfArticleByDateBetween(@Param("article_type")String article_type,@Param("tempDateBegin")Date tempDateBegin, @Param("tempDateEnd")Date tempDateEnd);

	List<Map<String, Integer>> selectArticleLabelList();

	Article[] selectArticleByLimitIndexAndSearchWords(@Param("startIndex")int startIndex, 
													@Param("pageScale")int pageScale, 
													@Param("paramList")List<String> paramList,
													@Param("article_type")String article_type,
													@Param("searchWords")String[] searchWords);

	int selectCountOfArticleBySearchWords(@Param("article_type")String article_type, 
										@Param("searchWords")String[] searchWords);


	Article[] selectArticleByLimitIndexAndDateBetween(@Param("startIndex")int startIndex, 
													@Param("pageScale")int pageScale, 
													@Param("paramList")List<String> paramList,
													@Param("article_type")String article_type, 
													@Param("beginDate")Date beginDate, 
													@Param("endDate")Date endDate);


	Article[] selectArticleByLimitIndexAndLabel(@Param("startIndex")int startIndex, 
												@Param("pageScale")int pageScale, 
												@Param("paramList")List<String> paramList, 
												@Param("article_type")String article_type,
												@Param("article_label")String article_label);


	int selectCountOfArticleByLabel(@Param("article_type")String article_type,
									@Param("article_label")String article_label);


}
