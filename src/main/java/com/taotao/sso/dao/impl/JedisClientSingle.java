/**
 * 
 */
package com.taotao.sso.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.dao.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * <p> Description: 
 * 		单机版操作redis的dao实现类
 * </p>
 * @author fengda
 * @date 2017年3月2日 下午5:56:38
 */
public class JedisClientSingle implements JedisClient {

	@Autowired
	private JedisPool JedisPool;
	
	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		Jedis jedis = JedisPool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#set(java.lang.String, java.lang.String)
	 */
	@Override
	public String set(String key, String value) {
		Jedis jedis = JedisPool.getResource();
		String string = jedis.set(key, value);
		jedis.close();
		return string;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hset(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public long hset(String hkey, String key, String value) {
		Jedis jedis = JedisPool.getResource();
		Long hsetnx = jedis.hsetnx(hkey, key, value);
		jedis.close();
		return hsetnx;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hget(java.lang.String, java.lang.String)
	 */
	@Override
	public String hget(String hkey, String key) {
		Jedis jedis = JedisPool.getResource();
		String hget = jedis.hget(hkey, key);
		jedis.close();
		return hget;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#incr(java.lang.String)
	 */
	@Override
	public long incr(String key) {
		Jedis jedis = JedisPool.getResource();
		Long incr = jedis.incr(key);
		jedis.close();
		return incr;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#expire(java.lang.String, long)
	 */
	@Override
	public long expire(String key, int second) {
		Jedis jedis = JedisPool.getResource();
		Long expire = jedis.expire(key, second);
		jedis.close();
		return expire;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#ttl(java.lang.String)
	 */
	@Override
	public long ttl(String key) {
		Jedis jedis = JedisPool.getResource();
		Long ttl = jedis.ttl(key);
		jedis.close();
		return ttl;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#del(java.lang.String)
	 */
	@Override
	public long del(String key) {
		Jedis jedis = JedisPool.getResource();
		Long del = jedis.del(key);
		jedis.close();
		return del;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hdel(java.lang.String, java.lang.String)
	 */
	@Override
	public long hdel(String hkey, String key) {
		Jedis jedis = JedisPool.getResource();
		Long hdel = jedis.hdel(hkey, key);
		jedis.close();
		return hdel;
	}

}
