package com.pengblog.dao;

import org.apache.ibatis.annotations.Param;

import com.pengblog.bean.IpObject;

public interface IipDao {

	int insert(IpObject ipObject);

	IpObject selectIpById(int id);

	void update(IpObject ipObject);

	IpObject selectIpByIP(String clientIP);

	IpObject[] selectIpListBeenBannedByLimitIndex(@Param("startIndex")int startIndex, @Param("pageScale")int pageScale);

	int selectCountOfIpObjectBeenBanned();

}
