package com.pengblog.serviceInterface;

import java.awt.image.BufferedImage;

import com.pengblog.bean.CaptchaResult;

public interface IcaptchaService {

	BufferedImage generateCaptchaImage(String captchaId);

	CaptchaResult checkCaptchaCode(String captchaId, String uncheckCaptchaCode);

}
