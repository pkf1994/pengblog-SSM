package com.pengblog.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.peng.annotation.RequireToken;
import com.pengblog.serviceInterface.ItxCosService;
import com.pengblog.utils.MyFileUtil;

@Controller
@RequestMapping("/image")
public class ImageController {
	
	
	@Autowired
	@Qualifier("txCosService")
	private ItxCosService txCosService;
	
	@RequireToken
	@RequestMapping(value="/image_upload.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object uploadImage(HttpServletRequest request, MultipartFile img) throws IOException {
		
		File imageFile = MyFileUtil.transforMultipartFileToFile(request, img);
		
		String fileName = MyFileUtil.addTimestampForFileName(imageFile.getName());
		
		//String finalUrl = qiniuService.uploadTempImageToQiniu(imageFile, fileName);
		
		String finalUrl = txCosService.uploadTempImage(imageFile, fileName);
		
		Gson gson = new Gson();
		
		Map<String,String> retJsonMap = new HashMap<>();
		
		retJsonMap.put("imgUrl", finalUrl);
		
		String retJson = gson.toJson(retJsonMap);
		
		return retJson;
	}

}
