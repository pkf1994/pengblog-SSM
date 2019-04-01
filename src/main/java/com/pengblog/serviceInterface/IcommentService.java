/**
 * 
 */
package com.pengblog.serviceInterface;

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
	int getMaxPage(int hostId, int pageScale);

	Comment[] getCommentList(int hostId, int startIndex, int pageScale);
	
	Comment[] getTopLevelCommentList(int hostId, int startIndex, int pageScale);

	int getCountOfComment(int article_id);
	
	int getCountOfSecondaryComment(int comment_id);

	Comment getCommentById(int comment_id);

	int saveComment(Comment comment);

	Comment constructComment(Map<String, String> commentData);

	List<Comment> getCommentLastListByLimitIndex(int startIndex, int pageScale);

	int getMaxPageOfComment(int pageScale);
	
	int getMaxPageOfTopLevelComment(int hostId, int pageScale);
	
	int getMaxPageOfSecondaryComment(int comment_id, int pageScale);

	int getCountOfCommentByArticleId(int article_id);
	
	void deleteCommentById(int comment_id);

	int getMaxPageOfSubComment(int comment_id, int pageScale);

	int getCountOfSubComment(int comment_id);

	Comment[] getSubCommentList(int comment_id, int startIndex, int pageScale);
	
	int getCountOfAllComment();

	Boolean checkWhetherNeedCaptcha(String clientIP);

	Comment[] getCommentList(int article_id, int startIndex, int pageScale, String token);

	Comment[] getTopLevelCommentList(int article_id, int startIndex, int pageScale, String token);

	Comment[] getSubCommentList(int comment_id, int startIndex, int pageScale, String token);
	

}
