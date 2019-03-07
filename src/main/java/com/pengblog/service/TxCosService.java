package com.pengblog.service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pengblog.utils.LogUtil;
import com.pengblog.utils.MyFileUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.CopyObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

@Service("txCosService")
public class TxCosService implements ItxCosService{
	
	private static final Logger logger = LogManager.getLogger(TxCosService.class);
	
	
	private static String cosDomainName;
	
	private static String clDomainName;
	
	private static String thumbnail;
	
	private static String blogImageBucket;
	
	private static COSCredentials cosCredentials;
	
	private static ClientConfig clientConfig;
	
	
	static {
		/*
		cosCredentials = new BasicCOSCredentials(secretId, secretKey);	
		
		clientConfig = new ClientConfig(new Region(region));*/
		
		
		
		/*//通过存储一个目录类“亚当”，来获取COSURL的path部分，以下部分代码参考官方SDK示例
		COSClient cosClient = new COSClient(cred, clientConfig);
		
		InputStream input = new ByteArrayInputStream(new byte[0]);
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		
		objectMetadata.setContentLength(0);

		PutObjectRequest putObjectRequest = 
				new PutObjectRequest(properties.getProperty("blogImageBucket"), "Adam/", input, objectMetadata);
		
		cosClient.putObject(putObjectRequest);
		
		Date expirationDate = new Date(new Date().getTime() + 1000L * 3600L);
		
		URL imageUrl = cosClient.generatePresignedUrl(properties.getProperty("blogImageBucket"), "Adam/", expirationDate);
		
		urlPath = "https://" + imageUrl.getHost() + "/";
		
		cosClient.shutdown();*/
		
	}

	@Override
	public String uploadTempImage(File imageFile, String fileName) {
		
		COSClient cosClient = new COSClient(cosCredentials, clientConfig);
		
		
		fileName = "temp/" + fileName;
		
		PutObjectRequest putObjectRequest = new PutObjectRequest(blogImageBucket, fileName, imageFile);
		
		cosClient.putObject(putObjectRequest);
		
		Date expirationDate = new Date(new Date().getTime() + 1000L * 3600L);
		
		URL imageUrl = cosClient.generatePresignedUrl(blogImageBucket, fileName, expirationDate);
		
		//返回URL路径部分，这是因为，当bucket为公共读时，预签名接口返回的url的?以前的部分即为永久有效对象链接
		String imageUrlStr = "https://" + imageUrl.getHost() + imageUrl.getPath();
		
		cosClient.shutdown();
		
		return imageUrlStr;
	}

	@Override
	public List<String> transferTempImageUrlList(List<String> imgUrls, Integer article_id) {
		
		COSClient cosClient = new COSClient(cosCredentials, clientConfig);
		
		
		List<String> handledImgUrls = new ArrayList<>();
		
		for(String imgUrl:imgUrls) {
			
			handledImgUrls.add(imgUrl);
			
			if(imgUrl.indexOf(cosDomainName) >= 0) {
				
				String tempImgName = imgUrl.replace(cosDomainName, "");
				
				String imgName = tempImgName.replace("temp/", article_id + "/");
				
			
				
				this.moveImage(blogImageBucket, tempImgName, blogImageBucket, imgName);
				
				logger.info(LogUtil.infoBegin);
				logger.info("转临时图片为正式图片: " + cosDomainName + tempImgName + "=>" + cosDomainName + imgName);
				logger.info( LogUtil.infoEnd);
				
				handledImgUrls.remove(handledImgUrls.size() - 1);
				
				handledImgUrls.add(cosDomainName + imgName);
				
			}
		}
		
		cosClient.shutdown();
		
		return handledImgUrls;
	}

	private void moveImage(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
		
		COSClient cosClient = new COSClient(cosCredentials, clientConfig);
		
		if(sourceBucketName == destinationBucketName && sourceKey == destinationKey) {
			return;
		}
		
		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
		
		cosClient.copyObject(copyObjectRequest);
		
		cosClient.deleteObject(sourceBucketName, sourceKey);

		cosClient.shutdown();
		
	}

	@Override
	public void deleteImage(List<String> imgUrlList, int article_id) {
		
		COSClient cosClient = new COSClient(cosCredentials, clientConfig);
		
		for(String imgUrl: imgUrlList) {
			
			if(imgUrl.indexOf(cosDomainName) >= 0) {
				
				String key = imgUrl.replace(cosDomainName, "");
				
				if(key.indexOf(article_id + "/") >= 0) {
					
					cosClient.deleteObject(blogImageBucket, key);
					
				}
				 
			}
			
		}
		
		cosClient.shutdown();
		
	}

	@Override
	public String thumbnail(String article_firstImageUrl) {
		// TODO Auto-generated method stub
		if(article_firstImageUrl.indexOf(cosDomainName) < 0) {
			return article_firstImageUrl;
		}
		return article_firstImageUrl.replace(cosDomainName, clDomainName) + thumbnail;
	}

	
	@Autowired
	public void setCosDomainName(String cosDomainName) {
		TxCosService.cosDomainName = cosDomainName;
	}
	
	@Autowired
	public void setClDomainName(String clDomainName) {
		TxCosService.clDomainName = clDomainName;
	}
	
	@Autowired
	public void setThumbnail(String thumbnail) {
		TxCosService.thumbnail = thumbnail;
	}
	
	@Autowired
	public void setBlogImageBucket(String blogImageBucket) {
		TxCosService.blogImageBucket = blogImageBucket;
	}
	
	@Autowired
	public void setCosCredentials(COSCredentials cosCredentials) {
		TxCosService.cosCredentials = cosCredentials;
	}
	
	@Autowired
	public void setClientConfig(ClientConfig clientConfig) {
		TxCosService.clientConfig = clientConfig;
	}
	
	

}
