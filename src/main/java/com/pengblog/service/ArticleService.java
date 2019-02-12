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
	@Qualifier("qiniuService")
	private IqiniuService qiniuService;
	
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
	public Article getArticleById(int article_id) {
		
		Article article = articleDao.selectArticleById(article_id);
		
		return article;
	}

	@Override
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
		
		article.setArticle_releaseTime(new Date());
		
		return article;
	}

	public Article handleImageUrl(Article article) {
		
		if(article.getArticle_content() == null || article.getArticle_content() == "") {
			return article;
		}
		
		List<String> imgUrls = MyHtmlUtil.extractImageUrlFromArticleContent(article.getArticle_content());
		
		//List<String> handledImgUrls = qiniuService.handleImageUrl(imgUrls, article.getArticle_id());
		
		List<String> handledImgUrls = txCosService.transferTempImageUrlList(imgUrls, article.getArticle_id());
		
		String article_content = article.getArticle_content();
		
		for(int i = 0; i < imgUrls.size(); i++) {
			article_content = article_content.replace(imgUrls.get(i), handledImgUrls.get(i));
		}
		
		article.setArticle_content(article_content);
		
		return article;
	}

	@Override
	public int saveArticle(Article article) {
		
		int article_id = articleDao.insertArticle(article);
		
		logger.info(LogUtil.infoBegin);
		logger.info("存储文章: " + article.getArticle_title() + "-" + article.getArticle_author() + "-" + article.getArticle_label());
		logger.info(LogUtil.infoEnd);
		
		return article_id;
	}

	@Override
	public Article[] getDraftList(int startIndex, int pageScale) {
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add(ArticleFields.ARTICLE_ID.fieldName);
		paramList.add(ArticleFields.ARTICLE_TITLE.fieldName);
		paramList.add(ArticleFields.ARTICLE_RELEASETIME.fieldName);

			
		Article[] articleList = articleDao.selectArticleListByLimitIndex(startIndex,pageScale,paramList,"draft");
		
		return articleList;
	}

	@Override
	public int getMaxPageOfDraft(int pageScale) {
		
		int countOfAllArticle = articleDao.selectCountOfArticle("draft");
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticle/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public void deleteArticleById(int article_id) {
		Article article = getArticleById(article_id);
		
		String article_content = article.getArticle_content();
		
		articleDao.deleteArticleById(article_id);
		
		logger.info(LogUtil.infoBegin);
		logger.info("删除文章: " + article.getArticle_title() + "-" + article.getArticle_author() + "-" + article.getArticle_label());
		logger.info(LogUtil.infoEnd);
		
		commentDao.deleteCommentByArticleId(article_id);
		
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
		
		
	}

	@Override
	public void updateArticle(Article handledArticle) {
		
		articleDao.updateArticle(handledArticle);
		
		logger.info(LogUtil.infoBegin);
		logger.info("com.pengblog.service.ArticleService", "更新文章: " + handledArticle.getArticle_title() + "-" + handledArticle.getArticle_author() + "-" + handledArticle.getArticle_label());
		logger.info(LogUtil.infoEnd);
		
	}

	@Override
	public Map<Integer, Object> getArticleFiling() {
		
		Map<Integer, Object> retMap = new HashMap<>();
		
		Calendar now = Calendar.getInstance();
		
		int yearNow = now.get(Calendar.YEAR);
		
		for(int i = 0; i < 10; i++) {
			
			Calendar tempCalendarBeginY = Calendar.getInstance();
			
			Calendar tempCalendarEndY = Calendar.getInstance();
			
			tempCalendarBeginY.set(yearNow - i, 0, 1);
			
			tempCalendarEndY.set(yearNow - i + 1, 0, 1);
			
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


	@Override
	public List<Map<String, Integer>> getArticleLabelList() {

		List<Map<String, Integer>> articleLabelList = articleDao.selectArticleLabelList();
		
		return articleLabelList;
	}

	@Override
	public Article[] getArticleItemListByLimitIndexAndSearchWords(int startIndex, int pageScale,
			String[] searchWords) {
		
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_summary");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
	
		Article[] articles = articleDao.selectArticleByLimitIndexAndSearchWords(startIndex,pageScale,paramList,"article",searchWords);
		
		return articles;
	}

	@Override
	public int getMaxPageBySearchWords(int pageScale, String[] searchWords) {
		
		int countOfAllArticleBySearchWords = articleDao.selectCountOfArticleBySearchWords("article",searchWords);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleBySearchWords/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public int getCountOfArticleBySearchWords(String[] searchWords) {
		
		int countOfAllArticleBySearchWords = articleDao.selectCountOfArticleBySearchWords("article",searchWords);
		
		return countOfAllArticleBySearchWords;
	}

	@Override
	public Article[] getArticleItemListByLimitIndexAndYearAndMonth(int startIndex, int pageScale, String selectedYear,
			String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
		
		endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
		
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

	@Override
	public int getMaxPageByYearAndMonth(int pageScale, String selectedYear, String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
		
		endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
		
		Date beginDate = beginCal.getTime();
		
		Date endDate = endCal.getTime();
		
		int countOfAllArticleByLimitDate = articleDao.selectCountOfArticleByDateBetween("article",beginDate,endDate);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleByLimitDate/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public int getCountOfArticleByYearAndMonth(String selectedYear, String selectedMonth) {
		
		Calendar beginCal = Calendar.getInstance();
		
		Calendar endCal = Calendar.getInstance();
		
		beginCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth) - 1, 1);
		
		endCal.set(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), 1);
		
		Date beginDate = beginCal.getTime();
		
		Date endDate = endCal.getTime();
		
		int countOfAllArticleByLimitDate = articleDao.selectCountOfArticleByDateBetween("article",beginDate,endDate);
		
		return countOfAllArticleByLimitDate;
	}

	@Override
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

	@Override
	public int getMaxPageByLabel(int pageScale, String article_label) {
		
		int countOfAllArticleByLabel = articleDao.selectCountOfArticleByLabel("article",article_label);
		
		int maxPage = (int) Math.ceil((double)(countOfAllArticleByLabel/pageScale)) + 1;
		
		return maxPage;
	}

	@Override
	public int getCountOfArticleByLabel(String article_label) {
		
		int countOfAllArticleByLabel = articleDao.selectCountOfArticleByLabel("article",article_label);
		
		return countOfAllArticleByLabel;
	}

	@Override
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
		
		if(doc.select("img[src]").size() > 0) {
			
			String article_firstImageUrl = doc.select("img[src]").first().attr("src");
			
			//String article_previewImageUrl = article_firstImageUrl + "?imageView2/1/w/200/h/150/interlace/1/q/53";
			
			String article_previewImageUrl = txCosService.thumbnail(article_firstImageUrl);
			
			article.setArticle_previewImageUrl(article_previewImageUrl);
			
		}
		
		return article;
		
	}

	
	public int getCountOfArticle() {
		
		return getCountOfArticle("article");
		
	}

	@Override
	public int getCountOfArticle(String article_type) {
		
		int countOfAllArticle = articleDao.selectCountOfArticle("article");
		
		return countOfAllArticle;
	}

	@Override
	public Article getDraft() {
		
		List<String> paramList = new ArrayList<>();
		
		paramList.add("article_id");
		paramList.add("article_title");
		paramList.add("article_author");
		paramList.add("article_releaseTime");
		paramList.add("article_label");
		paramList.add("article_previewImageUrl");
		paramList.add("article_content");
			
		Article[] articleList = articleDao.selectArticleListByLimitIndex(0,1,paramList,"draft");
		
		if(articleList.length == 0) {
			return new Article();
		}
		
		return articleList[0];
	}
	
}
