package com.pengblog.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyFileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;

@Service("imageService")
public class ImageService implements IimageService {
	
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
	public String uploadTempImageToQiniu(File imageFile, String fileName) {
				
		String bucket = properties.getProperty("tempImageBucket");
		
		String upToken = auth.uploadToken(bucket);
		
		try {
		    Response response = uploadManager.put(imageFile, fileName, upToken);
		    
		    String domainOfBucket = properties.getProperty("tempImageBucketUrl");
		    
		    String encodedFileName = URLEncoder.encode(fileName, "utf-8");
		    
		    String finalUrl = String.format("http://%s/%s", domainOfBucket, encodedFileName );
		    

		    
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
}
