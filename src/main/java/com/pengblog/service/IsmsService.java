package com.pengblog.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

import com.github.qcloudsms.httpclient.HTTPException;

public interface IsmsService {

	Map<String,Object> send(String phoneNumber) throws JSONException, HTTPException, IOException;

}
