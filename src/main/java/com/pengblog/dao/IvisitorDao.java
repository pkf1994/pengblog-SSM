package com.pengblog.dao;

import com.pengblog.bean.Visitor;

public interface IvisitorDao {

	int insertVisitor(Visitor visitor);

	Visitor selectVisitorByName(String visitor_name);

	void updateVisitor(Visitor visitor);

}
