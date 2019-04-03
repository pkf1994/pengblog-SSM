package com.pengblog.serviceInterface;

import com.pengblog.bean.IpObject;

public interface IipService {

	void banById(int i);

	IpObject save(String clientIP);

	IpObject getIpById(int ip_id);

	void ban(IpObject ipObject);

	IpObject getIpByIp(String ip);

	void lifted(IpObject ipObject);

	IpObject[] getIpObjectListBeenBannedByLimitIndex(int startIndex, int pageScale);

	int getCountOfIpObjectBeenBanned(int pageScale);

}
