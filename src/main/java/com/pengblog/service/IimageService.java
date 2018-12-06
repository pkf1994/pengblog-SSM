package com.pengblog.service;

import java.io.File;

public interface IimageService {

	String uploadTempImageToQiniu(File imageFile, String fileName);

}
