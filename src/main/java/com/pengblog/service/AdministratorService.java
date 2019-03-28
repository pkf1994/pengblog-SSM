package com.pengblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Administrator;
import com.pengblog.dao.IadministratorDao;
import com.pengblog.serviceInterface.IadministratorService;

@Service("administratorService")
public class AdministratorService implements IadministratorService{

	@Autowired
	private IadministratorDao administratorDao;
	
	@Override
	public Administrator getAdministratorById(int administrator_id) {
		
		Administrator administrator = administratorDao.selectAdministratorById(administrator_id);
		
		return administrator;
	}

}
