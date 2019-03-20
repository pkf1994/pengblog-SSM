/**
 * 
 */
package com.pengblog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Article;
import com.pengblog.bean.Comment;
import com.pengblog.bean.Visitor;
import com.pengblog.dao.IcommentDao;
import com.pengblog.interceptor.RecordClientIPInterceptor;
import com.pengblog.redis.RedisUtil;
import com.pengblog.utils.LogUtil;

/**
 * @author Administrator
 *
 */
@Service("commentService")
public class CommentService implements IcommentService{
	
	private static final Logger logger = LogManager.getLogger(CommentService.class);
	
	@Autowired
	private IcommentDao commentDao;
	

	public int getMaxPage(int hostId, int pageScale) {
		
		int countOfComment = commentDao.selectCountOfCommentByHostId(hostId);
		
		int maxPage = (int) Math.ceil((double)(countOfComment/pageScale)) + 1;
				
		return maxPage;
	}

	/* (non-Javadoc)
	 * @see com.pengblog.service.IcommentService#getCommentList(int, int, int)
	 */
	@Override
	public Comment[] getCommentList(int hostId, int startIndex, int pageScale) {
		
		Comment[] commentList = commentDao.selectCommentListByLimitIndex(hostId, startIndex, pageScale);
		
		return commentList;
	}

	@Override
	public int getCountOfComment(int hostId) {
		
		int countOfComment = commentDao.selectCountOfCommentByHostId(hostId);
		
		return countOfComment;
	}

	@Override
	public Comment getCommentById(int comment_id) {
		
		Comment comment = commentDao.selectCommentById(comment_id);
		
		return comment;
	}

	@Override
	public int saveComment(Comment comment) {
		
		int comment_id = commentDao.insertComment(comment);
		
		Comment referComment = comment.getComment_referComment();
				
		if(referComment != null) {
			referComment.setComment_haveSubComment(true);
			commentDao.updateComment(referComment);
		}
		
		logger.info(LogUtil.infoBegin);
		logger.info("保存评论: " + comment.getComment_author().getVisitor_name() + "-" + comment.getComment_content());
		logger.info(LogUtil.infoEnd);
		
		return comment_id;
	}


	@Override
	public Comment constructComment(Map<String, String> commentData) {
		
		Comment comment = new Comment();
		
		Visitor visitor = new Visitor();
		
		Article article = new Article();
		
		if(commentData.containsKey("visitor_name") && (commentData.get("visitor_name")!="")) {
			visitor.setVisitor_name(commentData.get("visitor_name"));
		}
		
		if(commentData.containsKey("comment_referComment") && (commentData.get("comment_referComment")!="")  && (commentData.get("comment_referComment") != null)) {
			Comment referComment = getCommentById(Integer.parseInt(commentData.get("comment_referComment")));
			if(referComment != null)
			comment.setComment_referComment(referComment);
		}
		
		if(commentData.containsKey("comment_content") && (commentData.get("comment_content")!="")) {
			comment.setComment_content(commentData.get("comment_content"));
		}
		
		if(commentData.containsKey("comment_hostId") && (commentData.get("comment_hostId")!="")) {
			
			article.setArticle_id(Integer.parseInt(commentData.get("comment_hostId")));

		}
		
		if(commentData.containsKey("visitor_email") && (commentData.get("visitor_email")!="")) {
			visitor.setVisitor_email(commentData.get("visitor_email"));
		}
		
		if(commentData.containsKey("visitor_siteAddress") && (commentData.get("visitor_siteAddress")!="")) {
			visitor.setVisitor_siteAddress(commentData.get("visitor_siteAddress"));
		}
		
		if(commentData.containsKey("comment_platform") && (commentData.get("comment_platform")!="")) {
			comment.setComment_platform(commentData.get("comment_platform"));
		}
		
		comment.setComment_hostArticle(article);
		
		comment.setComment_author(visitor);
		
		comment.setComment_releaseTime(new Date());
		
		return comment;
	}

	@Override
	public List<Comment> getCommentLastListByLimitIndex(int startIndex, int pageScale) {
		
		List<Comment> comments = commentDao.selectCommentLastListByLimitIndex(startIndex, pageScale);
		
		return comments;
	}

	@Override
	public int getMaxPageOfComment(int pageScale) {

		int countOfComment = commentDao.selectCountOfComment();
		
		int maxPage = (int) Math.ceil((double)(countOfComment/pageScale)) + 1;
				
		return maxPage;
	}

	@Override
	public int getCountOfCommentByArticleId(int article_id) {
		
		int count = commentDao.selectCountOfCommentByHostId(article_id);
		
		return count;
	}

	@Override
	public void deleteCommentById(int comment_id) {
		
		commentDao.deleteCommentById(comment_id);
		
		logger.info(LogUtil.infoBegin);
		logger.info("删除评论: id" + comment_id);
		logger.info(LogUtil.infoEnd);
		
	}

	@Override
	public Comment[] getTopLevelCommentList(int hostId, int startIndex, int pageScale) {
		Comment[] commentList = commentDao.selectTopLevelCommentListByLimitIndex(hostId, startIndex, pageScale);
		return commentList;
	}

	@Override
	public int getCountOfSecondaryComment(int comment_id) {
		int count = commentDao.selectCountOfSecondaryComment(comment_id);
		
		return count;
	}

	@Override
	public int getMaxPageOfTopLevelComment(int hostId, int pageScale) {
		
		int countOfTopLevelComment = commentDao.selectCountOfTopLevelCommentByHostId(hostId);
		
		int maxPage = (int) Math.ceil((double)(countOfTopLevelComment/pageScale)) + 1;
				
		return maxPage;
	}

	@Override
	public int getMaxPageOfSecondaryComment(int comment_id, int pageScale) {
		int countOfTopSecondaryComment = commentDao.selectCountOfSecondaryComment(comment_id);
		
		int maxPage = (int) Math.ceil((double)(countOfTopSecondaryComment/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public int getMaxPageOfSubComment(int comment_id, int pageScale) {
		
		int countOfSubComment = commentDao.selectCountOfSecondaryComment(comment_id);
		
		int maxPage = (int) Math.ceil((double)(countOfSubComment/pageScale)) + 1;
				
		return maxPage;
	}

	@Override
	public int getCountOfSubComment(int comment_id) {
		
		int countOfSubComment = commentDao.selectCountOfSecondaryComment(comment_id);
		
		return countOfSubComment;
	}

	@Override
	public Comment[] getSubCommentList(int comment_id, int startIndex, int pageScale) {
		
		Comment[] subCommentList = commentDao.selectSubCommentListByLimitIndex(comment_id, startIndex, pageScale);
		
		return subCommentList;
	}

	@Override
	public int getCountOfAllComment() {
		
		int countOfComment = commentDao.selectCountOfComment();
		
		return countOfComment;
	}

	@Override
	public Boolean checkWhetherNeedCaptcha(String clientIP) {
		
		//没有传来ip，需要输入验证码
		if(clientIP == null) {
			return true;
		}
		
		//从redis取出该ip最近提交评论次数
		String timesStr = RedisUtil.getStringKV(clientIP, RecordClientIPInterceptor.dbIndex);
		
		//redis中没有发现该ip
		if(timesStr == null) {
			return false;
		}
		
		int times = Integer.parseInt(timesStr);
		
		//该ip最近提交评论次数小于5，则不需要输入验证码
		if(times < 5) {
			return false;
		}
		
		//该ip最近提交评论次数大于等于5，需要输入验证码
		return true;
	}


}
