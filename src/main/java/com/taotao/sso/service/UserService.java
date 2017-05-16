/**
 * 
 */
package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * <p> Description: </p>
 * @author fengda
 * @date 2017年3月8日 下午5:06:38
 */
public interface UserService {
	
	public TaotaoResult checkData(String content, Integer type);
	
	public TaotaoResult createUser(TbUser user);
	
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);
	
	public TaotaoResult getUserByToken(String token);
	
	public TaotaoResult userLogout(String token);

}
