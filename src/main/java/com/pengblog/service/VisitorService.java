package com.pengblog.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Visitor;
import com.pengblog.dao.IvisitorDao;
import com.pengblog.utils.LogUtil;

@Service("visitorService")
public class VisitorService implements IvisitorService{
	
	private static final Logger logger = LogManager.getLogger(VisitorService.class);
	
	@Autowired
	private IvisitorDao visitorDao;

	@Override
	public Visitor saveVisitor(Visitor visitor) {
		
		Visitor _visitor = visitorDao.selectVisitorByName(visitor.getVisitor_name());
		
		if(_visitor != null) {
			
			if(_visitor.getVisitor_email().equals(visitor.getVisitor_email())) {
				
				visitor.setVisitor_id(_visitor.getVisitor_id());
				
				visitorDao.updateVisitor(visitor);
			}
			
			return visitor;
		}
		
		int visitor_id = visitorDao.insertVisitor(visitor);
		
		logger.info(LogUtil.infoBegin);
		logger.info("存储访客: " + visitor.getVisitor_name() + "-" + visitor.getVisitor_email());
		logger.info(LogUtil.infoEnd);
		
		return visitor;
	}
	
}
