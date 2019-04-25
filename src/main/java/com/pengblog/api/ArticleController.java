package com.pengblog.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.peng.annotation.FrontEndCacheable;
import com.peng.annotation.RequireToken;
import com.pengblog.bean.Article;
import com.pengblog.serviceInterface.IarticleService;
import com.pengblog.serviceInterface.IcommentService;

/**
 * @author Peng Kaifan
 * ��article�йصĽӿ�
 *
 */

@Controller
@RequestMapping("/article")
public class ArticleController {
	
	@Autowired
	@Qualifier("articleService")
	private IarticleService articleService;
	
	@Autowired
	@Qualifier("commentService")
	private IcommentService commentService;
	
	
	@FrontEndCacheable
	@RequestMapping(value="/article_summary.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleSummaryList(int startIndex,
										int pageScale) {
		
		Article[] articleList = articleService.getArticleSummaryList(startIndex, pageScale);
		
		for (int i = 0; i < articleList.length; i++) {
			int count = commentService.getCountOfCommentByArticleId(articleList[i].getArticle_id());
			articleList[i].setArticle_countOfAllComment(count);
		}
		
		int count = articleService.getCountOfArticle("article");
		
		int maxPage = articleService.getMaxPage(pageScale);
		
		Gson gson = new Gson();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("articleList", articleList);
		ret.put("count",count);
		ret.put("maxPage", maxPage);
		String retJson = gson.toJson(ret);
		
		return retJson;
	}
	
	@RequestMapping(value="/draft.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getDraft() {
		
		Article draft = articleService.getDraft();
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(draft);
		
		return retJson;
	}
	
	@FrontEndCacheable
	@RequestMapping(value="/article.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticle(int article_id) throws Exception {
		
		Article article = articleService.getArticleById(article_id);
		
		if(article == null) {
			return ReturnVo.err("non-existent");
		}
		
		return ReturnVo.ok(article);
	}
	
	
	
	@RequestMapping(value="/upload_article.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadArticle(@RequestBody Map<String,String> articleData) {
		
		Article article = articleService.constructArticle(articleData);
		

 		if(article.getArticle_id() == null || article.getArticle_id() == 0) {
			articleService.saveArticle(article);
		}
		
		if(article.getArticle_type().equals("article")) {
			
			article = articleService.handleImageUrl(article);
			
			articleService.handlePreviewImage(article);
			
		}
		
		articleService.updateArticle(article);
		
		return article.getArticle_id();
	}
	
	@RequireToken
	@RequestMapping(value="/draft_list.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getDraftList(int startIndex,
								int pageScale) {
		Article[] articleList = articleService.getDraftList(startIndex,pageScale);
		
		int count = articleService.getCountOfArticle("draft");
		
		int maxPage = articleService.getMaxPageOfDraft(pageScale);
		
		Gson gson = new Gson();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("articleList", articleList);
		ret.put("count",count);
		ret.put("maxPage", maxPage);
		String retJson = gson.toJson(ret);
		
		return retJson;
	}
	
	@RequestMapping(value="/delete_article.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteArticle(int article_id) {
		
		articleService.deleteArticleById(article_id);
		
		return "delete success";
	}
	
	@RequestMapping(value="/delete_article_list.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object deleteArticleList(@RequestBody Map<String,String> deleteArticleListData) {
		
		Gson gson = new Gson();
		
		int [] article_ids = gson.fromJson(deleteArticleListData.get("articleIdListString"), int[].class);
		
		for (int i = 0; i < article_ids.length; i++) {
			
			articleService.deleteArticleById(article_ids[i]);
			
		}
		
		return "delete success";
	}
	
	@FrontEndCacheable
	@RequestMapping(value="/article_filing.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleFiling() {
		
		Map<Integer,Object> articleFilingMap = articleService.getArticleFiling();
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(articleFilingMap);
		
		return retJson;
	}
	
	@FrontEndCacheable
	@RequestMapping(value="/article_label.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleLabelList() {
		
		List<Map<String, Integer>> articleLabelList = articleService.getArticleLabelList();
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(articleLabelList);
		
		return retJson;
	}
	
	@RequestMapping(value="/article_bysearch.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleListByLimitIndexAndSearchWords(int startIndex, int pageScale, String searchString) {
		
		String[] searchWords = searchString.split("\\s+");
		
		Article[] articles = articleService.getArticleItemListByLimitIndexAndSearchWords(startIndex, pageScale, searchWords);
		
		int maxPage = articleService.getMaxPageBySearchWords(pageScale, searchWords);
		
		int countOfAllArticleBySearchWords = articleService.getCountOfArticleBySearchWords(searchWords);
		
		Map<String, Object> retMap = new HashMap<>();
		
		retMap.put("maxPage", maxPage);
		
		retMap.put("articleList", articles);
		
		retMap.put("countOfAllArticleBySearchWords", countOfAllArticleBySearchWords);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retMap);
		
		return retJson;
	}
	
	@RequestMapping(value="/article_byfiling.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleListByLimitIndexAndFilingDate(int startIndex, int pageScale, String selectedYear, String selectedMonth) {
		
		Article[] articles = articleService.getArticleItemListByLimitIndexAndYearAndMonth(startIndex, pageScale, selectedYear, selectedMonth);
		
		int maxPage = articleService.getMaxPageByYearAndMonth(pageScale, selectedYear, selectedMonth);
		
		int countOfAllArticleByFilingDate = articleService.getCountOfArticleByYearAndMonth(selectedYear, selectedMonth);
		
		Map<String, Object> retMap = new HashMap<>();
		
		retMap.put("maxPage", maxPage);
		
		retMap.put("articleList", articles);
		
		retMap.put("countOfAllArticleByFilingDate", countOfAllArticleByFilingDate);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retMap);
		
		return retJson;
	}
	
	@RequestMapping(value="/article_bylabel.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getArticleListByLimitIndexAndLabel(int startIndex, int pageScale, String article_label) {
		
		Article[] articles = articleService.getArticleItemListByLimitIndexAndLabel(startIndex, pageScale, article_label);
		
		int maxPage = articleService.getMaxPageByLabel(pageScale, article_label);
		
		int countOfAllArticleByLabel = articleService.getCountOfArticleByLabel(article_label);
		
		Map<String, Object> retMap = new HashMap<>();
		
		retMap.put("maxPage", maxPage);
		
		retMap.put("articleList", articles);
		
		retMap.put("countOfAllArticleByLabel", countOfAllArticleByLabel);
		
		Gson gson = new Gson();
		
		String retJson = gson.toJson(retMap);
		
		return retJson;
		
	}
	
	@RequireToken
	@FrontEndCacheable
	@RequestMapping(value="/get_deleted_article_list.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getDeletedArticleList(int startIndex,
										int pageScale) throws Exception {
		
		int count = articleService.getCountOfDeletedArticleList();
		
		if(count == 0) {
			return ReturnVo.ok(null);
		}
		
		if(startIndex < 0 || startIndex > (count - 1)) {
			return ReturnVo.err("bad startIndex");
		}
		
		if(pageScale < 1) {
			return ReturnVo.err("bad pageScale");
		}
		
		Article[] articleList = articleService.getDeletedArticleList(startIndex, pageScale);
		
		int maxPage = articleService.getMaxPageOfDeletedArticle(pageScale);
		
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("articleList", articleList);
		ret.put("maxPage", maxPage);
		
		return ReturnVo.ok(ret);

	}
	
	@RequireToken
	@RequestMapping(value="/recover_article.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object recoverArticle(int article_id) throws Exception {
		
		articleService.recoverArticle(article_id);
		
		return ReturnVo.ok("recover successfully");

	}
	
	@RequireToken
	@RequestMapping(value="/destroy_all_article_deleted.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object destroyAllArticleDeleted() throws Exception {
		
		articleService.destroyAllArticleDeleted();
		
		return ReturnVo.ok("clean successfully");

	}
}
