package com.pengblog.api;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peng.annotation.RequireToken;
import com.pengblog.bean.IpObject;
import com.pengblog.serviceInterface.IipService;

@Controller
@RequestMapping("/ip")
public class IpController {
	
	@Autowired
	@Qualifier("ipService")
	private IipService ipService;
	
	@RequireToken
	@RequestMapping(value="/ban_byid.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object banById(int ip_id) throws Exception {
		
		IpObject ipObject = ipService.getIpById(ip_id);
		
		if(ipObject == null) {
			return ReturnVo.err("bad ip_id");
		}
		
		ipService.ban(ipObject);
		
		return ReturnVo.ok("ban IP successfully");
	}
	
	@RequireToken
	@RequestMapping(value="/ban_byip.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object banById(String ip) throws Exception {
		
		IpObject ipObject = ipService.getIpByIp(ip);
		
		if(ipObject == null) {
			return ReturnVo.err("bad ip");
		}
		
		ipService.ban(ipObject);
		
		return ReturnVo.ok("ban IP successfully");
	}
	
	@RequireToken
	@RequestMapping(value="/lifted_byip.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object liftedById(String ip) throws Exception {
		
		IpObject ipObject = ipService.getIpByIp(ip);
		
		if(ipObject == null) {
			return ReturnVo.err("bad ip");
		}
		
		ipService.lifted(ipObject);
		
		return ReturnVo.ok("ban IP successfully");
	}
	
	@RequireToken
	@RequestMapping(value="/ip_list_been_banned.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getIpListBeenBanned(int startIndex, int pageScale) throws Exception {
		
		if(startIndex < 0) {
			return ReturnVo.err("bad startIndex");
		}
		
		if(pageScale < 1) {
			return ReturnVo.err("bad pageScale");
		}
		
		IpObject[] ipList = ipService.getIpObjectListBeenBannedByLimitIndex(startIndex, pageScale);
		
		int maxPage = ipService.getCountOfIpObjectBeenBanned(pageScale);
		
		int count = ipService.getCountOfIpObjectBeenBanned(pageScale);
		
		Map<String,Object> retMap = new HashMap<>();
		
		retMap.put("ipList", ipList);
		
		retMap.put("maxPage", maxPage);
		
		retMap.put("count", count);
		
		return ReturnVo.ok(retMap);
	}
}
