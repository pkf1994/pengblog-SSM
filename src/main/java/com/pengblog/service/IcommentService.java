/**
 * 
 */
package com.pengblog.service;

import java.util.List;
import java.util.Map;

import com.pengblog.bean.Comment;

/**
 * @author Administrator
 *
 */
public interface IcommentService {

	/**
	 * @param pageScale
	 * @return
	 */
	int getMaxPage(int hostid, int pageScale);

	/**
	 * @param currentPage
	 * @param pageScale
	 * @return
	 */
	Comment[] getCommentList(int hostid, int startIndex, int pageScale);

	int getCountOfComment(int article_id);

	Comment getCommentById(int comment_id);

	int saveComment(Comment comment);

	Comment constructComment(Map<String, String> commentData);

	List<Comment> getCommentLastListByLimitIndex(int startIndex, int pageScale);

	int getMaxPageOfCommentLast(int pageScale);

	int getCountOfCommentByArticleId(int article_id);

	void deleteCommentById(int comment_id);

}
