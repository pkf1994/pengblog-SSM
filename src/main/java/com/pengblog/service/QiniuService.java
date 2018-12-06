package com.pengblog.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyFileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

@Service("qiniuService")
public class QiniuService implements IqiniuService{
	
private static final Logger logger = LogManager.getLogger(QiniuService.class);
	
private static Configuration cfg;
	
	private static UploadManager uploadManager;
	
	private static String accessKey;
	
	private static String secretKey;
	
	private static Auth auth;
	
	private static Properties properties;
	
	
	static {
		cfg = new Configuration(Zone.zone2());
		
		uploadManager = new UploadManager(cfg);
		
		String propertiesPath = (ImageService.class.getResource("/") + "qiniu.properties").substring(5);
		
		properties = MyFileUtil.loadProperties(propertiesPath);
		
	    accessKey = properties.getProperty("accessKey");
	    
	    secretKey  = properties.getProperty("secretKey");
	    
	    auth = Auth.create(accessKey, secretKey);
	}


	@Override
	public List<String> handleImageUrl(List<String> imgUrls, int article_id) {
		// TODO Auto-generated method stub
		//http://pgqgagyc3.bkt.clouddn.com/tempImage201810191200133.jpg
		List<String> handledImgUrls = new ArrayList<>();
		
		for(String imgUrl:imgUrls) {
			
			handledImgUrls.add(imgUrl);
			
			if(imgUrl.indexOf("http://pgqgagyc3.bkt.clouddn.com/") >= 0) {
				
				String tempImgName = imgUrl.replace("http://pgqgagyc3.bkt.clouddn.com/", "");
				
				String imgName = article_id + "/" +tempImgName;
				
				moveImage("tempimage", tempImgName, "blogimage", imgName);
				
				logger.info(LogUtil.infoBegin);
				logger.info("转临时图片为正式图片: " + "http://pgqgagyc3.bkt.clouddn.com/" + tempImgName + "=>" + "http://pd861shos.bkt.clouddn.com/" + imgName);
				logger.info(LogUtil.infoEnd);
				
				handledImgUrls.remove(handledImgUrls.size() - 1);
				
				handledImgUrls.add("http://pd861shos.bkt.clouddn.com/" + imgName);
				
			}
		}
		return handledImgUrls;
	}

	private void moveImage(String fromBucket, String fromKey, String toBucket, String toKey) {
		// TODO Auto-generated method stub
		
		BucketManager bucketManager = new BucketManager(auth, cfg);
		
		try {
			bucketManager.move(fromBucket, fromKey, toBucket, toKey);
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String uploadTempImageToQiniu(File imageFile, String fileName) {
				
		String bucket = properties.getProperty("tempImageBucket");
		
		String upToken = auth.uploadToken(bucket);
		
		try {
		    Response response = uploadManager.put(imageFile, fileName, upToken);
		    
		    String domainOfBucket = properties.getProperty("tempImageBucketUrl");
		    
		    String encodedFileName = URLEncoder.encode(fileName, "utf-8");
		    
		    String finalUrl = String.format("http://%s/%s", domainOfBucket, encodedFileName );
		    
		    logger.info(LogUtil.infoBegin);
		    logger.info("上传临时图片到七牛: " + finalUrl);
		    logger.info(LogUtil.infoEnd);
		    
		    imageFile.delete();
		    
		    /*Thread.sleep(1000);*/
		    //解析上传成功的结果
		    return finalUrl;
		    
		} catch (QiniuException ex) {
			
			Response r = ex.response;
			
			imageFile.delete();
			
			return r.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			imageFile.delete();
			
			return "uploadTempImageErr";
		}/* catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "uploadTempImageErr";
		}*/
	}

	@Override
	public void deleteImage(List<String> imgUrlList) {
		
		BucketManager bucketManager = new BucketManager(auth, cfg);
		
		for(String imgUrl: imgUrlList) {
			
			String key = imgUrl.replace("http://pd861shos.bkt.clouddn.com/", "");
			
			try {
				
				bucketManager.delete("blogimage", key);
				
				logger.info(LogUtil.infoBegin);
				logger.info("删除七牛图片: " + "http://pd861shos.bkt.clouddn.com/" + key);
				logger.info(LogUtil.infoEnd);
				
			} catch (QiniuException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
	}


}
