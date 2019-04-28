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
											@Param("article_type")String article_type,
											@Param("deletedStatus")int deletedStatus);

	
	int selectCountOfArticle(@Param("article_type")String article_type,
							@Param("deletedStatus")int deletedStatus);

	
	Article selectArticleById(int article_id);

	int insertArticle(Article article);


	void deleteArticleById(@Param("article_id")int article_id, 
							@Param("article_deleteTime")Date date);

	void updateArticle(Article handledArticle);

	int selectCountOfArticleByDateBetween(@Param("article_type")String article_type,
											@Param("tempDateBegin")Date tempDateBegin, 
											@Param("tempDateEnd")Date tempDateEnd, 
											@Param("deletedStatus")int deletedStatus);

	List<Map<String, Integer>> selectArticleLabelList(@Param("deletedStatus")int deletedStatus);

	Article[] selectArticleByLimitIndexAndSearchWords(@Param("startIndex")int startIndex, 
													@Param("pageScale")int pageScale, 
													@Param("paramList")List<String> paramList,
													@Param("article_type")String article_type,
													@Param("searchWords")String[] searchWords, 
													@Param("deletedStatus")int deletedStatus);

	int selectCountOfArticleBySearchWords(@Param("article_type")String article_type, 
										@Param("searchWords")String[] searchWords, 
										@Param("deletedStatus")int deletedStatus);


	Article[] selectArticleByLimitIndexAndDateBetween(@Param("startIndex")int startIndex, 
													@Param("pageScale")int pageScale, 
													@Param("paramList")List<String> paramList,
													@Param("article_type")String article_type, 
													@Param("beginDate")Date beginDate, 
													@Param("endDate")Date endDate, 
													@Param("deletedStatus")int deletedStatus);


	Article[] selectArticleByLimitIndexAndLabel(@Param("startIndex")int startIndex, 
												@Param("pageScale")int pageScale, 
												@Param("paramList")List<String> paramList, 
												@Param("article_type")String article_type,
												@Param("article_label")String article_label, 
												@Param("deletedStatus")int deletedStatus);


	int selectCountOfArticleByLabel(@Param("article_type")String article_type,
									@Param("article_label")String article_label, 
									@Param("deletedStatus")int deletedStatus);


	int selectCountOfDeletedArticle(String string);


	Article[] selectDeletedArticleByLimitIndex(@Param("startIndex")int startIndex, 
												@Param("pageScale")int pageScale, 
												@Param("paramList")List<String> paramList);


	void destroyArticleById(int article_id);


	void recoverArticleById(int article_id);


	Integer[] selectAllArticleIdDeleted();


	


}
