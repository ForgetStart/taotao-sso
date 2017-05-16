/**
 * 
 */
package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;

/**
 * <p> Description: 
 * 		用户管理
 * </p>
 * @author fengda
 * @date 2017年3月8日 下午5:08:55
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	//用户信息在redis中存储的key值
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	
	//用户信息在redis中存储的key值得有效期
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	
	@Autowired
	private JedisClient jedisClient;
	
	/**
	 * 检查用户名是否可用(是否有重复)
	 * type类型	 1、2、3分别代表username、phone、email
	 */
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//用户名校验
		if(type == 1){
			criteria.andUsernameEqualTo(content);
		//电话校验
		}else if(type == 2){
			criteria.andPhoneEqualTo(content);
		//email校验
		}else{
			criteria.andEmailEqualTo(content);
		}
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list.isEmpty() || list == null || list.size() ==0){
			return TaotaoResult.ok(true);
		}
		
		return TaotaoResult.ok(false);
	}

	/**
	 * 创建新用户(用户注册)
	 */
	@Override
	public TaotaoResult createUser(TbUser user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		//对用户密码进行md5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		tbUserMapper.insert(user);
		
		return TaotaoResult.ok();
	}

	/**
	 * 判断用户是否登录成功
	 * 接收用户名和密码，到数据库中查询，根据用户名查询用户信息，查到之后进行密码比对，
	 * 需要对密码进行md5加密后进行比对。比对成功后说明登录成功，
	 * 需要生成一个token可以使用UUID。
	 * 需要把用户信息写入redis，key就是token，value就是用户信息。返回token字符串。
	 */
	@Override
	public TaotaoResult userLogin(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
		List<TbUser> list = tbUserMapper.selectByExample(example);
		
		if(list.isEmpty() || list == null || list.size() == 0){
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		
		//生成token
		String token = UUID.randomUUID().toString();
		TbUser user = list.get(0);
		//保存用户之前，把用户对象中的密码清空。为了安全
		user.setPassword(null);
		//将用户信息保存到redis中
		jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		//设置key的有效期
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		
		//添加写cookie的逻辑，cookie的有效期是关闭浏览器就失效。
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		
		//返回token
		return TaotaoResult.ok(token);
	}

	/**
	 * 通过token查询用户信息
	 * 根据token判断用户是否登录或者session是否过期。接收token，根据token到redis中取用户信息。
	 * 判断token字符串是否对应用户信息，如果不对应说明token非法或者session已过期。
	 * 取到了说明用户就是正常的登录状态。返回用户信息，同时重置用户的过期时间。
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		String userInfo = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		if(StringUtils.isBlank(userInfo)){
			return TaotaoResult.build(400, "此用户session已经过期，请重新登录");
		}
		//重置用户信息的有效时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		return TaotaoResult.ok(JsonUtils.jsonToPojo(userInfo, TbUser.class));
		
	}

	/**
	 * 用户安全退出
	 */
	@Override
	public TaotaoResult userLogout(String token) {
		
		try {
			String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
			if(StringUtils.isNotBlank(json)){
				jedisClient.del(REDIS_USER_SESSION_KEY + ":" + token); 
			}
			return TaotaoResult.ok();
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
