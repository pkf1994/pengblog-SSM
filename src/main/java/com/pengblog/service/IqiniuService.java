package com.pengblog.service;

import java.io.File;
import java.util.List;

public interface IqiniuService {

	List<String> handleImageUrl(List<String> imgUrls, int article_id);

	String uploadTempImageToQiniu(File imageFile, String fileName);

	void deleteImage(List<String> imgUrlList);

}
