package com.pengblog.serviceInterface;

import java.io.File;
import java.util.List;

public interface ItxCosService {

	String uploadTempImage(File imageFile, String fileName);

	List<String> transferTempImageUrlList(List<String> imgUrls, Integer article_id);

	void deleteImage(List<String> imgUrlList, int article_id);

	String thumbnail(String article_firstImageUrl);

}
