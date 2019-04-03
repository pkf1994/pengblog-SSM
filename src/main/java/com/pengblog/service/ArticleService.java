package com.pengblog.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pengblog.bean.Article;
import com.pengblog.dao.IarticleDao;
import com.pengblog.dao.IcommentDao;
import com.pengblog.serviceInterface.IarticleService;
import com.pengblog.serviceInterface.ItxCosService;
import com.pengblog.utils.ArticleFields;
import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyHtmlUtil;

/**
 * @author Administrator
 *
 */
@Service("articleService")
public class ArticleService implements IarticleService{
	
	private static final Logger logger = LogManager.getLogger(ArticleService.class);
	
	@Autowired
	private IarticleDao articleDao;
	
	@Autowired
	private IcommentDao commentDao;

	@Autowired
	@Qualifier("txCosService")
	private ItxCosService txCosService;
	
	/* (non-Javadoc)
	 * @see com.pengblog.service.IarticleService#getArticleSummary(int, int)
	 */
	@Transactional
	public Article[] getArticleSummaryList(int startIndex, int pageScale) {
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_summary");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
		paramList.add("article_previewImageUrl");
		paramList.add("article_titleImageUrl");

			
		Article[] articleList = articleDao.selectArticleListByLimitIndex(startIndex,pageScale,paramList,"article");
		
		return articleList;
	}

	/* (non-Javadoc)
	 * @see com.pengblog.service.IarticleService#getMaxPage(int)
	 */
	@Transactional
	public int getMaxPage(int pageScale) {
		
		int countOfAllArticle = articleDao.selectCountOfArticle("article");
		
		if(countOfAllArticle % pageScale == 0) {
			
			return (int)countOfAllArticle/pageScale;
			
		}
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticle/pageScale)) + 1;
		
		return maxPage;
	}

	/* (non-Javadoc)
	 * @see com.pengblog.service.IarticleService#getArticleById(int)
	 */
	@Override
	@Transactional
	public Article getArticleById(int article_id) {
		
		Article article = articleDao.selectArticleById(article_id);
		
		return article;
	}

	@Override
	@Transactional
	public Article constructArticle(Map<String, String> articleData) {
		
		Article article = new Article();
		
		if(articleData.containsKey("article_id") && (articleData.get("article_id")!="")) {
			article.setArticle_id(Integer.parseInt(articleData.get("article_id")));
		}
		
		if(articleData.containsKey("article_title") && (articleData.get("article_title")!="")) {
			article.setArticle_title(articleData.get("article_title"));
		}
		
		if(articleData.containsKey("article_author")) {
			article.setArticle_author(articleData.get("article_author"));
		}
		
		if(articleData.containsKey("article_label")) {
			article.setArticle_label(articleData.get("article_label"));
		}
		
		if(articleData.containsKey("article_content")) {
			
			if(articleData.get("article_content") == "") {
				article.setArticle_content("");
				article.setArticle_summary("");
			}else {
				
				article.setArticle_content(articleData.get("article_content"));
				
				String article_content = article.getArticle_content();
				
				Document doc = Jsoup.parse(article_content);
				
				String article_content_text = doc.body().text();
				
				int length = article_content_text.length();
				
				if(length <= 200) {
					
					String article_summary = article_content_text;
					
					article.setArticle_summary(article_summary);
					
				}else {
					String article_summary = article_content_text.substring(0,200);
					
					article.setArticle_summary(article_summary);
				}
			}
		}
		
		if(articleData.containsKey("article_type") && (articleData.get("article_type")!="")) {
			article.setArticle_type(articleData.get("article_type"));
		}
		
		if(articleData.containsKey("article_titleImageUrl")) {
			article.setArticle_titleImageUrl(articleData.get("article_titleImageUrl"));
		}
		
		article.setArticle_releaseTime(new Date());
		
		return article;
	}

	@Transactional
	public Article handleImageUrl(Article article) {
		
		if(article.getArticle_content() == null || article.getArticle_content() == "") {
			return article;
		}
		
		List<String> imgUrls = MyHtmlUtil.extractImageUrlFromArticleContent(article.getArticle_content());
		
		String titleImageUrl = article.getArticle_titleImageUrl();
		
		if(titleImageUrl != null && titleImageUrl != "") {
			imgUrls.add(titleImageUrl);
		}
		
		//List<String> handledImgUrls = qiniuService.handleImageUrl(imgUrls, article.getArticle_id());
		
		List<String> handledImgUrls = txCosService.transferTempImageUrlList(imgUrls, article.getArticle_id());
		
		String article_content = article.getArticle_content();
		
		for(int i = 0; i < imgUrls.size(); i++) {
			article_content = article_content.replace(imgUrls.get(i), handledImgUrls.get(i));
		}
		
		article.setArticle_content(article_content);
		
		if(titleImageUrl != null && titleImageUrl != "") {
			article.setArticle_titleImageUrl(handledImgUrls.get(handledImgUrls.size()-1));
		}
		
		return article;
	}

	@Transactional
	public int saveArticle(Article article) {
		
		int article_id = articleDao.insertArticle(article);
		
		logger.info(LogUtil.infoBegin);
		logger.info("存储文章: " + article.getArticle_title() + "-" + article.getArticle_author() + "-" + article.getArticle_label());
		logger.info(LogUtil.infoEnd);
		
		return article_id;
	}

	@Transactional
	public Article[] getDraftList(int startIndex, int pageScale) {
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add(ArticleFields.ARTICLE_ID.fieldName);
		paramList.add(ArticleFields.ARTICLE_TITLE.fieldName);
		paramList.add(ArticleFields.ARTICLE_RELEASETIME.fieldName);

			
		Article[] articleList = articleDao.selectArticleListByLimitIndex(startIndex,pageScale,paramList,"draft");
		
		return articleList;
	}

	@Transactional
	public int getMaxPageOfDraft(int pageScale) {
		
		int countOfAllArticle = articleDao.selectCountOfArticle("draft");
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticle/pageScale)) + 1;
		
		return maxPage;
	}

	@Transactional
	public void deleteArticleById(int article_id) {
		
		commentDao.deleteCommentByArticleId(article_id);
		
		articleDao.deleteArticleById(article_id, new Date());
		
		logger.info(LogUtil.infoBegin);
		logger.info("删除文章: " + article_id);
		logger.info(LogUtil.infoEnd);
		
		
	}
	
	@Transactional
	public void destroyArticleById(int article_id) {
		
		commentDao.destroyCommentByArticleId(article_id);
		
		Article article = getArticleById(article_id);
		
		String article_content = article.getArticle_content();
		
		Document doc = Jsoup.parse(article_content);
		
		List<String> imgUrlList = new ArrayList<>();
		
		if(doc.select("img[src]").size() > 0) {
			
			Elements els = doc.select("img[src]");
			
			for(Element el: els) {
				imgUrlList.add(el.attr("src"));
			}
			
		}
		
		//qiniuService.deleteImage(imgUrlList);
		txCosService.deleteImage(imgUrlList, article_id);
		
		articleDao.destroyArticleById(article_id);
		
		logger.info(LogUtil.infoBegin);
		logger.info("清除文章: " + article_id);
		logger.info(LogUtil.infoEnd);
	}

	@Transactional
	public void updateArticle(Article handledArticle) {
		
		articleDao.updateArticle(handledArticle);
		
		logger.info(LogUtil.infoBegin);
		logger.info("com.pengblog.service.ArticleService", "更新文章: " + handledArticle.getArticle_title() + "-" + handledArticle.getArticle_author() + "-" + handledArticle.getArticle_label());
		logger.info(LogUtil.infoEnd);
		
	}

	@Transactional
	public Map<Integer, Object> getArticleFiling() {
		
		Map<Integer, Object> retMap = new HashMap<>();
		
		Calendar now = Calendar.getInstance();
		
		int yearNow = now.get(Calendar.YEAR);
		
		for(int i = 0; i < 10; i++) {
			
			Calendar tempCalendarBeginY = Calendar.getInstance();
			
			Calendar tempCalendarEndY = Calendar.getInstance();
			
			tempCalendarBeginY.set(yearNow - i, 0, 1);
			
			tempCalendarEndY.set(yearNow - i + 1, 0, 1);
			
			tempCalendarBeginY.set(Calendar.HOUR_OF_DAY, 0);
			tempCalendarBeginY.set(Calendar.MINUTE, 0);
			tempCalendarBeginY.set(Calendar.SECOND, 0);
			tempCalendarBeginY.set(Calendar.MILLISECOND, 0);
			tempCalendarEndY.set(Calendar.HOUR_OF_DAY, 0);
			tempCalendarEndY.set(Calendar.MINUTE, 0);
			tempCalendarEndY.set(Calendar.SECOND, 0);
			tempCalendarEndY.set(Calendar.MILLISECOND, 0);
			
			Date tempDateBeginY = tempCalendarBeginY.getTime();
			
			Date tempDateEndY = tempCalendarEndY.getTime();
			
			int countY = articleDao.selectCountOfArticleByDateBetween("article", tempDateBeginY, tempDateEndY);
			
			if(countY > 0) {
				
				List<Integer> monthList = new ArrayList<>();
				
				for(int j = 0; j < 12; j++) {

					Calendar tempCalendarBeginM = Calendar.getInstance();
					
					Calendar tempCalendarEndM = Calendar.getInstance();
				
					tempCalendarBeginM.set(yearNow - i, j, 1);
					
					tempCalendarEndM.set(yearNow - i, j + 1, 1);
					
					tempCalendarBeginM.set(Calendar.HOUR_OF_DAY, 0);
					tempCalendarBeginM.set(Calendar.MINUTE, 0);
					tempCalendarBeginM.set(Calendar.SECOND, 0);
					tempCalendarBeginM.set(Calendar.MILLISECOND, 0);
					tempCalendarEndM.set(Calendar.HOUR_OF_DAY, 0);
					tempCalendarEndM.set(Calendar.MINUTE, 0);
					tempCalendarEndM.set(Calendar.SECOND, 0);
					tempCalendarEndM.set(Calendar.MILLISECOND, 0);
					
					Date tempDateBeginM = tempCalendarBeginM.getTime();
					
					Date tempDateEndM = tempCalendarEndM.getTime();
					
					int countM = articleDao.selectCountOfArticleByDateBetween("article", tempDateBeginM, tempDateEndM);
					
					if(countM > 0) {
						monthList.add(tempCalendarBeginM.get(Calendar.MONTH) + 1);
					}
				}
				
				retMap.put(tempCalendarBeginY.get(Calendar.YEAR), monthList);
				
			}
		}
		
		return retMap;
	}


	@Transactional
	public List<Map<String, Integer>> getArticleLabelList() {

		List<Map<String, Integer>> articleLabelList = articleDao.selectArticleLabelList();
		
		return articleLabelList;
	}

	@Transactional
	public Article[] getArticleItemListByLimitIndexAndSearchWords(int startIndex, int pageScale,
			String[] searchWords) {
		
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_summary");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
		paramList.add("article_previewImageUrl");
		paramList.add("article_titleImageUrl");
	
		Article[] articles = articleDao.selectArticleByLimitIndexAndSearchWords(startIndex,pageScale,paramList,"article",searchWords);
		
		return articles;
	}

	@Transactional
	public int getMaxPageBySearchWords(int pageScale, String[] searchWords) {
		
		int countOfAllArticleBySearchWords = articleDao.selectCountOfArticleBySearchWords("article",searchWords);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleBySearchWords/pageScale)) + 1;
		
		return maxPage;
	}

	@Transactional
	public int getCountOfArticleBySearchWords(String[] searchWords) {
		
		int countOfAllArticleBySearchWords = articleDao.selectCountOfArticleBySearchWords("article",searchWords);
		
		return countOfAllArticleBySearchWords;
	}

	@Transactional
	public Article[] getArticleItemListByLimitIndexAndYearAndMonth( int startIndex, 
																	int pageScale, 
																	String selectedYear,
																	String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		if(selectedMonth == null || selectedMonth.equals("")) {
			
			beginCal.set(Integer.parseInt(selectedYear), 0, 1);
			
			endCal.set(Integer.parseInt(selectedYear), 11, 30);
			
		}else {
			
			beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
			
			endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
			
			if(Integer.parseInt(selectedMonth) == 2) {
				
				endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 28);
			}
		}
		
		beginCal.set(Calendar.HOUR_OF_DAY, 0);
		beginCal.set(Calendar.MINUTE, 0);
		beginCal.set(Calendar.SECOND, 0);
		beginCal.set(Calendar.MILLISECOND, 0);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		
		Date beginDate = beginCal.getTime();
		
		Date endDate = endCal.getTime();
				
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
		
		Article[] articles = articleDao.selectArticleByLimitIndexAndDateBetween(startIndex,pageScale,paramList,"article",beginDate,endDate);
		
		return articles;
	}

	@Transactional
	public int getMaxPageByYearAndMonth(int pageScale, String selectedYear, String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		if(selectedMonth == null || selectedMonth.equals("")) {
			
			beginCal.set(Integer.parseInt(selectedYear), 0, 1);
			
			endCal.set(Integer.parseInt(selectedYear), 11, 30);
			
		}else {
			
			beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
			
			endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
			
			if(Integer.parseInt(selectedMonth) == 2) {
				
				endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 28);
			}
		}
		
		beginCal.set(Calendar.HOUR_OF_DAY, 0);
		beginCal.set(Calendar.MINUTE, 0);
		beginCal.set(Calendar.SECOND, 0);
		beginCal.set(Calendar.MILLISECOND, 0);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		
		Date beginDate = beginCal.getTime();
		
		Date endDate = endCal.getTime();
		
		int countOfAllArticleByLimitDate = articleDao.selectCountOfArticleByDateBetween("article",beginDate,endDate);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleByLimitDate/pageScale)) + 1;
		
		return maxPage;
	}

	@Transactional
	public int getCountOfArticleByYearAndMonth(String selectedYear, String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		if(selectedMonth == null || selectedMonth.equals("")) {
			
			beginCal.set(Integer.parseInt(selectedYear), 0, 1);
			
			endCal.set(Integer.parseInt(selectedYear), 11, 30);
			
		}else {
			
			beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
			
			endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
			
			if(Integer.parseInt(selectedMonth) == 2) {
				
				endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 28);
			}
		}
		
		beginCal.set(Calendar.HOUR_OF_DAY, 0);
		beginCal.set(Calendar.MINUTE, 0);
		beginCal.set(Calendar.SECOND, 0);
		beginCal.set(Calendar.MILLISECOND, 0);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		
		Date beginDate = beginCal.getTime();
		
		Date endDate = endCal.getTime();
		
		int countOfAllArticleByLimitDate = articleDao.selectCountOfArticleByDateBetween("article",beginDate,endDate);
		
		return countOfAllArticleByLimitDate;
	}

	@Transactional
	public Article[] getArticleItemListByLimitIndexAndLabel(int startIndex, int pageScale, String article_label) {

		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_releaseTime");
		paramList.add("article_label");

		Article[] articles = articleDao.selectArticleByLimitIndexAndLabel(startIndex, pageScale, paramList, "article", article_label);
		
		return articles;
	}

	@Transactional
	public int getMaxPageByLabel(int pageScale, String article_label) {
		
		int countOfAllArticleByLabel = articleDao.selectCountOfArticleByLabel("article",article_label);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleByLabel/pageScale)) + 1;
		
		return maxPage;
	}

	@Transactional
	public int getCountOfArticleByLabel(String article_label) {
		
		int countOfAllArticleByLabel = articleDao.selectCountOfArticleByLabel("article",article_label);
		
		return countOfAllArticleByLabel;
	}

	@Transactional
	public Article handlePreviewImage(Article article) {
		
		if(article.getArticle_content() == null || article.getArticle_content() == "") {
			return article;
		}
		
		String article_content = article.getArticle_content();
		
		Document doc = Jsoup.parse(article_content);
		
		String article_content_text = doc.body().text();
		
		int length = article_content_text.length();
		
		if(length <= 200) {
			
			String article_summary = article_content_text;
			
			article.setArticle_summary(article_summary);
			
		}else {
			String article_summary = article_content_text.substring(0,200);
			
			article.setArticle_summary(article_summary);
		}
		
		if(article.getArticle_titleImageUrl() != null && article.getArticle_titleImageUrl() != "") {
			
			String article_previewImageUrl = txCosService.thumbnail(article.getArticle_titleImageUrl());
			
			article.setArticle_previewImageUrl(article_previewImageUrl);
			
		}else if(doc.select("img[src]").size() > 0) {
			
			String article_firstImageUrl = doc.select("img[src]").first().attr("src");
			
			//String article_previewImageUrl = article_firstImageUrl + "?imageView2/1/w/200/h/150/interlace/1/q/53";
			
			String article_previewImageUrl = txCosService.thumbnail(article_firstImageUrl);
			
			article.setArticle_previewImageUrl(article_previewImageUrl);	
		}
		
		return article;
		
	}

	@Transactional
	public int getCountOfArticle() {
		
		return getCountOfArticle("article");
		
	}

	@Transactional
	public int getCountOfArticle(String article_type) {
		
		int countOfAllArticle = articleDao.selectCountOfArticle("article");
		
		return countOfAllArticle;
	}

	@Transactional
	public Article getDraft() {
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
		paramList.add("article_previewImageUrl");
		paramList.add("article_content");
		paramList.add("article_titleImageUrl");
			
		Article[] articleList = articleDao.selectArticleListByLimitIndex(0,1,paramList,"draft");
		
		if(articleList.length == 0) {
			return new Article();
		}
		
		return articleList[0];
	}

	@Override
	public int getCountOfDeletedArticleList() {
		
		int countOfDeletedArticle = articleDao.selectCountOfDeletedArticle("article");
		
		return countOfDeletedArticle;
	}

	@Override
	public Article[] getDeletedArticleList(int startIndex, int pageScale) {
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_deleteTime");
		paramList.add("article_label");

		Article[] articles = articleDao.selectDeletedArticleByLimitIndex(startIndex, pageScale, paramList);
		
		return articles;	
	}

	@Override
	public int getMaxPageOfDeletedArticle(int pageScale) {
		int countOfDeletedArticle = articleDao.selectCountOfDeletedArticle("article");
		
		int maxPage = (int) Math.ceil((double)(countOfDeletedArticle/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public void recoverArticle(int article_id) {
		
		articleDao.recoverArticleById(article_id);
		
		
	}

	@Override
	public void destroyAllArticleDeleted() {
		 
		Integer[] ids = articleDao.selectAllArticleIdDeleted();
		
		for (int i = 0; i < ids.length; i++) {
			destroyArticleById(ids[i]);
		}
		
	}
	
}
