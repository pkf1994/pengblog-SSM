package com.pengblog.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengblog.bean.Visitor;
import com.pengblog.dao.IvisitorDao;
import com.pengblog.serviceInterface.IvisitorService;
import com.pengblog.utils.LogUtil;

@Service("visitorService")
public class VisitorService implements IvisitorService{
	
	private static final Logger logger = LogManager.getLogger(VisitorService.class);
	
	@Autowired
	private IvisitorDao visitorDao;

	@Override
	public Visitor saveVisitor(Visitor visitor) {
		
		Visitor[] _visitors = visitorDao.selectVisitorByName(visitor.getVisitor_name());
		
		if(_visitors != null) {
			
			for (int i = 0; i < _visitors.length; i++) {
				
				if(_visitors[i].getVisitor_email().equals(visitor.getVisitor_email())) {
					
					return _visitors[i];
				}
				
			}
			
			visitorDao.insertVisitor(visitor);
			
			logger.info(LogUtil.infoBegin);
			logger.info("存储访客: " + visitor.getVisitor_name() + "-" + visitor.getVisitor_email());
			logger.info(LogUtil.infoEnd);
			
			return visitor;
		}
		
		visitorDao.insertVisitor(visitor);
		
		logger.info(LogUtil.infoBegin);
		logger.info("存储访客: " + visitor.getVisitor_name() + "-" + visitor.getVisitor_email());
		logger.info(LogUtil.infoEnd);
		
		return visitor;
	}
	
}
