package com.pengblog.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUtil {
	
	private static JedisPool jedisPool;

	@Autowired
	public void setJedisPool(JedisPool jedisPool) {
		RedisUtil.jedisPool = jedisPool;
	}

	public static void setStringKV(String key, String value, int expire, int dbIndex) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(dbIndex);
		jedis.set(key, value);
		jedis.expire(key, expire);
		jedis.close();
	}
	
	public static String getStringKV(String key, int dbIndex) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(dbIndex);
		String retString = jedis.get(key);
		jedis.close();
		return retString;
	}

}
