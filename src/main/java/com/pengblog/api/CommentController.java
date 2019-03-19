/**
 * 
 */
package com.pengblog.api;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.peng.annotation.RecordClientIP;
import com.peng.annotation.RequireToken;
import com.pengblog.bean.Comment;
import com.pengblog.bean.SubmitCommentResult;
import com.pengblog.bean.Visitor;
import com.pengblog.interceptor.RecordClientIPInterceptor;
import com.pengblog.redis.RedisUtil;
import com.pengblog.service.IcommentService;
import com.pengblog.service.IvisitorService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	@Qualifier("commentService")
	private IcommentService commentService;
	
	@Autowired
	@Qualifier("visitorService")
	private IvisitorService visitorService;

	@RequestMapping(value="/comment_list.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCommentList(int article_id, int startIndex, int pageScale) {
		
		int maxPage = commentService.getMaxPage(article_id, pageScale);
		
		int countOfComment = commentService.getCountOfComment(article_id);
		
		Comment[] commentList = commentService.getCommentList(article_id, startIndex, pageScale);
		
		Gson gson = new Gson();
		Map<String,Object> ret = new HashMap<>();
		ret.put("maxPage", maxPage);
		ret.put("commentList", commentList);
		ret.put("countOfComment", countOfComment);
		String retJson = gson.toJson(ret);
		
		System.out.println(retJson);
		
		return retJson;
	}
	

	@RequestMapping(value="/top_level_comment_list.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getTopLevelCommentList(int article_id, int startIndex, int pageScale) {
		
		int maxPage = commentService.getMaxPageOfTopLevelComment(article_id, pageScale);
		
		int countOfComment = commentService.getCountOfComment(article_id);
		
		Comment[] commentList = commentService.getTopLevelCommentList(article_id, startIndex, pageScale);
		
		Gson gson = new Gson();
		Map<String,Object> ret = new HashMap<>();
		ret.put("maxPage", maxPage);
		ret.put("commentList", commentList);
		ret.put("countOfComment", countOfComment);
		String retJson = gson.toJson(ret);
		
		System.out.println(retJson);
		
		return retJson;
	}
	
	@RequestMapping(value="/sub_comment_list.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getSubCommentList(int comment_id, int startIndex, int pageScale) {
		
		int maxPage = commentService.getMaxPageOfSubComment(comment_id, pageScale);
		
		int countOfSubComment = commentService.getCountOfSubComment(comment_id);
		
		Comment[] subCommentList = commentService.getSubCommentList(comment_id, startIndex, pageScale);
		
		Gson gson = new Gson();
		Map<String,Object> ret = new HashMap<>();
		ret.put("maxPage", maxPage);
		ret.put("subCommentList", subCommentList);
		ret.put("countOfSubComment", countOfSubComment);
		String retJson = gson.toJson(ret);
		
		System.out.println(retJson);
		
		return retJson;
	}
	
	@RequestMapping(value="/comment.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getComment(int comment_id) {
		
		Comment ret = commentService.getCommentById(comment_id);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(ret);
		
		return retJson;
	}
	
	@RecordClientIP
	@RequestMapping(value="/submit_comment.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object submitComment(@RequestBody Map<String, String> commentData, HttpServletRequest request) throws UnsupportedEncodingException {
		
		Comment comment = commentService.constructComment(commentData);
		
		Visitor visitor = comment.getComment_author();
		
		visitor = visitorService.saveVisitor(visitor);
		
		comment.setComment_author(visitor);
		
		commentService.saveComment(comment);
		
		SubmitCommentResult submitCommentResult = new SubmitCommentResult();
		
		submitCommentResult.setSuccess(true);
		
		submitCommentResult.setCommentIdJustSubmit(comment.getComment_id());
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(submitCommentResult);
	
		return retJson;
		
	}
	
	@RequestMapping(value="/comment_last.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCommentLast(int startIndex, int pageScale) {
		
		int countOfComment = commentService.getCountOfAllComment();
		
		if(countOfComment < startIndex) {
			
			return "no more item available";
			
		}
		
		List<Comment> comments = commentService.getCommentLastListByLimitIndex(startIndex, pageScale);
		
		int maxPage = commentService.getMaxPageOfComment(pageScale);
		
		Map<String, Object> retMap = new HashMap<>();
	
		retMap.put("commentList", comments);
		
		retMap.put("maxPage", maxPage);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retMap);
		
		return retJson;
	}
	
	@RequestMapping(value="/comment_count.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getCommentCount(int article_id) {
		
		int count = commentService.getCountOfCommentByArticleId(article_id);
		
		return count;
	}
	
	@RequireToken
	@RequestMapping(value="/comment_delete.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteCommentById(int comment_id) {
		
		commentService.deleteCommentById(comment_id);
		
		return "delete comment successful";
	}
	
	@RequestMapping(value="/check_whether_need_captcha.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object checkWhetherNeedCaptcha(HttpServletRequest request) {
		
		String clientIP = request.getHeader("X-Real-IP");
		
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


