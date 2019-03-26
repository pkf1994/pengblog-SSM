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
import com.peng.annotation.FrontEndCacheable;
import com.peng.annotation.RecordClientIP;
import com.peng.annotation.RequireToken;
import com.pengblog.bean.CaptchaResult;
import com.pengblog.bean.Comment;
import com.pengblog.bean.SubmitCommentResult;
import com.pengblog.bean.Visitor;
import com.pengblog.interceptor.RecordClientIPInterceptor;
import com.pengblog.redis.RedisUtil;
import com.pengblog.service.CommentService;
import com.pengblog.service.IcaptchaService;
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
	
	@Autowired
	@Qualifier("captchaService")
	private IcaptchaService captchaService;
	

	@FrontEndCacheable
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
	
	@FrontEndCacheable
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
	
	@FrontEndCacheable
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
	
	@FrontEndCacheable
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
		
		String clientIP = request.getHeader("X-Real-IP");
		
		Boolean needCaptcha = commentService.checkWhetherNeedCaptcha(clientIP);
		
		SubmitCommentResult submitCommentResult = new SubmitCommentResult();
		
		if(needCaptcha) {
			
			String captchaId = commentData.get("captchaId");
			
			String captchaCode = commentData.get("captchaCode");
			
			CaptchaResult captchaResult = captchaService.checkCaptchaCode(captchaId,captchaCode);
			
			if(captchaResult.getPass() == false) {
				
				submitCommentResult.setSuccess(false);
				
				submitCommentResult.setMessage("绕过验证码的非法提交");
				
				Gson gson = new Gson();
				
				String retJson = gson.toJson(submitCommentResult);
				
				return retJson;
				
			}
		}
		
		Comment comment = commentService.constructComment(commentData);
		
		Visitor visitor = comment.getComment_author();
		
		visitor = visitorService.saveVisitor(visitor);
		
		comment.setComment_author(visitor);
		
		commentService.saveComment(comment);
		
		
		submitCommentResult.setSuccess(true);
		
		submitCommentResult.setCommentIdJustSubmit(comment.getComment_id());
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(submitCommentResult);
	
		return retJson;
		
	}
	
	@FrontEndCacheable
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
	
	@FrontEndCacheable
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
		
		Boolean needCaptcha = commentService.checkWhetherNeedCaptcha(clientIP);
		
		return needCaptcha;
	}
	
}


