package com.pengblog.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

import com.github.qcloudsms.httpclient.HTTPException;
import com.pengblog.bean.SendSmsResult;

public interface IsmsService {

	SendSmsResult send(String phoneNumber) throws JSONException, HTTPException, IOException;

}
