package com.pengblog.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyHtmlUtil {
	
	public static List<String> extractImageUrlFromArticleContent(String article_content) {
		
		Document doc = Jsoup.parse(article_content);
		
		Elements imgEls = doc.select("img[src]");
		
		List<String> imgSrcs = new ArrayList<>();
		
		for (int i = 0; i < imgEls.size(); i++) {
			
			imgSrcs.add(imgEls.get(i).attr("src"));
			
		}
		
		return imgSrcs;
	}
	
}
