package com.pengblog.dao;

import com.pengblog.bean.Administrator;

public interface IadministratorDao {

	Administrator selectAdministratorByUsername(String username);

	Administrator selectAdministratorById(int administrator_id);

}
