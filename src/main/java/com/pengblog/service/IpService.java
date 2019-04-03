package com.pengblog.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.pengblog.bean.IpObject;
import com.pengblog.dao.IipDao;
import com.pengblog.serviceInterface.IipService;

@Service("ipService")
public class IpService implements IipService {
	
	
	@Autowired
	private IipDao ipDao;
	
	@Override
	public void ban(IpObject ipObject) {
		
		ipObject.setIp_isBanned(true);
		
		ipDao.update(ipObject);
		
	}


	@Override
	public void banById(int id) {
		
		IpObject ipObject = ipDao.selectIpById(id);
		
		ipObject.setIp_isBanned(true);
		
		ipDao.update(ipObject);
		
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public IpObject save(String clientIP) {
	
		
		IpObject ipObject = ipDao.selectIpByIP(clientIP);
		
		if(ipObject != null) {
			return ipObject;
		}
		
		ipObject = new IpObject();
		ipObject.setIp_ip(clientIP);
		ipObject.setIp_banTime(new Date());
		
		int ip_id = ipDao.insert(ipObject);
		
		ipObject.setIp_id(ip_id);
		
		return ipObject;
	}

	@Override
	public IpObject getIpById(int ip_id) {
		
		IpObject ipObject = ipDao.selectIpById(ip_id);
		
		return ipObject;
	}


	@Override
	public IpObject getIpByIp(String ip) {
		// TODO Auto-generated method stub
		IpObject ipObject = ipDao.selectIpByIP(ip);
		
		return ipObject;
	}


	@Override
	public void lifted(IpObject ipObject) {
		ipObject.setIp_isBanned(false);
		
		ipDao.update(ipObject);
	}


	@Override
	public IpObject[] getIpObjectListBeenBannedByLimitIndex(int startIndex, int pageScale) {
		
		IpObject[] ipOjectList = ipDao.selectIpListBeenBannedByLimitIndex(startIndex,pageScale);
		
		return ipOjectList;
	}


	@Override
	public int getCountOfIpObjectBeenBanned(int pageScale) {
		
		int count = ipDao.selectCountOfIpObjectBeenBanned();
		
		if(count % pageScale == 0) {
			
			return (int)count/pageScale;
			
		}
		
		int maxPage = (int) Math.ceil((double)(count/pageScale)) + 1;
		
		return maxPage;
	}



}
