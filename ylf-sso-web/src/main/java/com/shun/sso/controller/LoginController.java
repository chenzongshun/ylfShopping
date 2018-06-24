package com.shun.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shun.common.pojo.ylfResult;
import com.shun.common.utils.CookieUtils;
import com.shun.sso.service.LoginService;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午3:10:18 
*/
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Value(value="${YLF_USER_COOKIE}")
	private String YLF_USER_COOKIE;

	/**
	 * 跳转到登录界面
	 * @param redirectSrc 这个是订单项目的拦截器发过来的<br>
	 * 						如果拦截器拦截到用户没有登陆就会重定向到这个登陆系统<br>
	 * 						并以xxx.xxx?redirectSrc="地址"的形式将回调的地址也一并传过来了
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/page/login")
	public String showLogin(String redirectSrc, HttpServletRequest request) {
		request.setAttribute("redirect", redirectSrc);
		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public ylfResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		// 执行登陆方法
		ylfResult result = loginService.login(username, password);
		// 验证是否登陆成功，如果登陆成功就将令牌取出来存放到cookie
		if (result.getStatus() == 200) {
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, YLF_USER_COOKIE, token);
		}
		return result;
	}
}
