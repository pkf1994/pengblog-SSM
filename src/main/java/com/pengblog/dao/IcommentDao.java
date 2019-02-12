/**
 * 
 */
package com.pengblog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pengblog.bean.Comment;

/**
 * @author Administrator
 *
 */
public interface IcommentDao {
	
	int selectCountOfCommentByHostId(@Param("hostId")int hostId);
	
	int selectCountOfTopLevelCommentByHostId(@Param("hostId")int hostId);
	
	Comment[] selectCommentListByLimitIndex(@Param("hostId")int hostId, @Param("startIndex")int startIndex, @Param("pageScale")int pageScale);

	Comment selectCommentById(int comment_id);

	int insertComment(Comment comment);

	void deleteCommentByArticleId(int article_id);

	List<Comment> selectCommentLastListByLimitIndex(@Param("startIndex")int startIndex, 
													@Param("pageScale")int pageScale);

	int selectCountOfComment();
	
	int selectCountOfSecondaryComment(int comment_id);

	void deleteCommentById(int comment_id);

	Comment[] selectTopLevelCommentListByLimitIndex(@Param("hostId")int hostId, @Param("startIndex")int startIndex, @Param("pageScale")int pageScale);

	Comment[] selectSubCommentListByLimitIndex(@Param("comment_id")int comment_id, @Param("startIndex")int startIndex, @Param("pageScale")int pageScale);

	void updateComment(Comment referComment);

	
}
