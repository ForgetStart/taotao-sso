/**
 * 
 */
package com.taotao.sso.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.dao.JedisClient;

import redis.clients.jedis.JedisCluster;


/**
 * <p> Description: 
 * 	集群版操作redis的dao实现类
 * </p>
 * @author fengda
 * @date 2017年3月2日 下午6:09:20
 */
public class JedisClientCluster implements JedisClient{

	@Autowired
	private JedisCluster jedisCluster;
	
	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		String string = jedisCluster.get(key);
		return string;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#set(java.lang.String, java.lang.String)
	 */
	@Override
	public String set(String key, String value) {
		String string = jedisCluster.set(key, value);
		return string;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hset(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public long hset(String hkey, String key, String value) {
		Long hset = jedisCluster.hset(hkey, key, value);
		return hset;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hget(java.lang.String, java.lang.String)
	 */
	@Override
	public String hget(String hkey, String key) {
		String hget = jedisCluster.hget(hkey, key);
		return hget;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#incr(java.lang.String)
	 */
	@Override
	public long incr(String key) {
		Long incr = jedisCluster.incr(key);
		return incr;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#expire(java.lang.String, int)
	 */
	@Override
	public long expire(String key, int second) {
		Long expire = jedisCluster.expire(key, second);
		return expire;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#ttl(java.lang.String)
	 */
	@Override
	public long ttl(String key) {
		Long ttl = jedisCluster.ttl(key);
		return ttl;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#del(java.lang.String)
	 */
	@Override
	public long del(String key) {
		Long del = jedisCluster.del(key);
		return del;
	}

	/* (non-Javadoc)
	 * @see com.taotao.rest.dao.JedisClient#hdel(java.lang.String, java.lang.String)
	 */
	@Override
	public long hdel(String hkey, String key) {
		Long hdel = jedisCluster.hdel(hkey, key);
		return hdel;
	}

}
