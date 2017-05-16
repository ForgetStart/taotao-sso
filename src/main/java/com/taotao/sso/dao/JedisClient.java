/**
 * 
 */
package com.taotao.sso.dao;

/**
 * <p> Description: </p>
 * @author fengda
 * @date 2017年3月2日 下午5:44:48
 */
public interface JedisClient {

	String get(String key);
	String set(String key, String value);
	
	long hset(String hkey, String key, String value);
	String hget(String hkey, String key);
	
	long incr(String key);
	long expire(String key, int second);	//设置key的过期时间
	long ttl(String key);					//查看key的过期时间
	
	long del(String key);
	
	long hdel(String hkey, String key);
}
