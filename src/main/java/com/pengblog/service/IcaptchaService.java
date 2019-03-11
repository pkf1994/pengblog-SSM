package com.pengblog.service;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IcaptchaService {

	BufferedImage generateCaptchaImage(String captchaId);

	Map<String, Object> checkCaptchaCode(String captchaId, String uncheckCaptchaCode);

}
