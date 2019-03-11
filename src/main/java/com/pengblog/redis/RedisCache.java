package com.pengblog.redis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Component
public class RedisCache implements Cache {
	
	private static final Logger logger = LogManager.getLogger(RedisCache.class);
	
	private static JedisConnectionFactory jedisConnectionFactory;
	
	private int dbIndex = 15;

    private final String id;
    
    /**
     * The {@code ReadWriteLock}.
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    public RedisCache() {
    	this.id = null;
    }
    
    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void putObject(Object key, Object value) {
		
		JedisConnection connection = null;
        try
        {
            logger.info(">>>>>>>>>>>>>>>>>>>>>>>>putObject:"+key+"="+value);
            connection = jedisConnectionFactory.getConnection();
            connection.select(dbIndex);
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(); //借用spring_data_redis.jar中的JdkSerializationRedisSerializer.class
            connection.set(serializer.serialize(key), serializer.serialize(value)); //利用其序列化方法将数据写入redis服务的缓存中
            
        }
        catch (JedisConnectionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null) {
                connection.close();
            }
        }

	}

	@Override
	public Object getObject(Object key) {
		
		 Object result = null;
	        JedisConnection connection = null;
	        try
	        {
	            connection = jedisConnectionFactory.getConnection();
	            connection.select(dbIndex);
	            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(); //借用spring_data_redis.jar中的JdkSerializationRedisSerializer.class
	            result = serializer.deserialize(connection.get(serializer.serialize(key))); //利用其反序列化方法获取值
	        }
	        catch (JedisConnectionException e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            if (connection != null) {
	                connection.close();
	            }
	        }
	        return result;
	}

	@Override
	public Object removeObject(Object key) {
		  JedisConnection connection = null;
		        Object result = null;
		        try
		        {
		            connection = jedisConnectionFactory.getConnection();
		            connection.select(dbIndex);
		            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
		            result =connection.expire(serializer.serialize(key), 0);
		        }
		        catch (JedisConnectionException e)
		        {
		            e.printStackTrace();
		        }
		        finally
		        {
		            if (connection != null) {
		                connection.close();
		            }
		        }
		        return result;
	}

	@Override
	public void clear() {
		
		JedisConnection connection = null;
        try
        {
            connection = jedisConnectionFactory.getConnection();
            connection.select(dbIndex);
            connection.flushDb();
        }
        catch (JedisConnectionException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null) {
                connection.close();
            }
        }

	}

	@Override
	public int getSize() {
		 int result = 0;
	        JedisConnection connection = null;
	        try
	        {
	            connection = jedisConnectionFactory.getConnection();
	            connection.select(dbIndex);
	            result = Integer.valueOf(connection.dbSize().toString());
	        }
	        catch (JedisConnectionException e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            if (connection != null) {
	                connection.close();
	            }
	        }
	        return result;
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		
		return this.readWriteLock;
	}
	
	
	@Autowired
	public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
		// TODO Auto-generated method stub
		RedisCache.jedisConnectionFactory = jedisConnectionFactory;
	}

}
