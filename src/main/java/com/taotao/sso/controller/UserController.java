/**
 * 
 */
package com.taotao.sso.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * <p> Description: </p>
 * @author fengda
 * @date 2017年3月8日 下午5:55:40
 */
@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	/**
	 * 验证用户信息，支持jsonp的调用
	 * @param param
	 * @param type
	 * @param callback
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback){
		TaotaoResult result = null;
		
		//参数有效性校验
		if(StringUtils.isBlank(param)){
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if(type == null){
			result = TaotaoResult.build(400, "校验类型不能为空");
		}
		if(type != 1 && type != 2 && type != 3){
			result = TaotaoResult.build(400, "校验类型错误");
		}
		
		//校验出错
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result; 
			}
		}
		
		
		try {
			result = userService.checkData(param, type);
		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		
		//支持jsonp的调用
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result; 
		}
	}
	
	
	/**
	 * 创建用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createUser(TbUser user){
		try {
			TaotaoResult result = userService.createUser(user);
			return TaotaoResult.ok();
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){
		
		if(StringUtils.isBlank(username)){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		if(StringUtils.isBlank(password)){
			return TaotaoResult.build(400, "密码不能为空");
		}
		
		try {
			TaotaoResult result = userService.userLogin(username, password, request, response);
			return result;
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	
	/**
	 * 通过token查询用户信息
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback){
		TaotaoResult result = null;
		
		try {
			result = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		//判断是否是jsonp的调用,支持jsonp的返回值
		if(StringUtils.isNotBlank(callback)){		//包装jsonp的格式
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		
		return result;
	}
	
	
	/**
	 * 用户安全退出
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public Object userLogout(@PathVariable String token, String callback){
		TaotaoResult result = null;
		
		try {
			result = userService.userLogout(token);
		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		
		return result;
	}
}
