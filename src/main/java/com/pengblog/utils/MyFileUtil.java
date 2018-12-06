package com.pengblog.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class MyFileUtil {
	  /**
     * MultipartFile转File
     * @param tempfile
     * @return
     */
    public static File transforMultipartFileToFile(HttpServletRequest request, MultipartFile multipartfile) throws IOException { 

       
        if(!multipartfile.isEmpty()){

        	  String contextpath = request.getContextPath()+"/tempfileDir";
              File f = new File(contextpath); 
              if(!f.exists()){
                  f.mkdirs();
              }
              
              String filename = multipartfile.getOriginalFilename();
              String filepath = contextpath+"/"+filename;
              File file = new File(filepath);

              //将MultipartFile文件转换，即写入File新文件中，返回File文件
              CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) multipartfile;
              commonsmultipartfile.transferTo(file);
              
             return file;
        }
        
        return null;
    }
    
    public static String addTimestampForFileName(String fileName) {
    	
    	int lastIndexOfDot = fileName.lastIndexOf(".");
    	
    	String suffix = fileName.substring(lastIndexOfDot);
    	
    	String fileNameWithTimestamp = "image" + new SimpleDateFormat("yyyyMMddhhmmmss").format(new Date()) + suffix;
    	
    	return fileNameWithTimestamp;
   	
    }
    
    public static Properties loadProperties(String propertiesPath) {
    	
    	Properties properties = new Properties();
	    // 使用ClassLoader加载properties配置文件生成对应的输入流
	    try {
			properties.load(new BufferedReader(new FileReader(propertiesPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return properties;
    }
}
