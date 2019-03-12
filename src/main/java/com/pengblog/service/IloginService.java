package com.pengblog.service;


import com.pengblog.bean.LoginResult;

public interface IloginService {

	LoginResult login(String username, String password);

	LoginResult loginDynamic(String phoneNumber, String dynamicPassword);

}
