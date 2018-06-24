package com.shun.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.shun.cart.service.CartService;
import com.shun.common.pojo.ylfResult;
import com.shun.common.utils.CookieUtils;
import com.shun.common.utils.JackJsonUtils;
import com.shun.pojo.TbItem;
import com.shun.pojo.TbUser;
import com.shun.sso.service.TokenService;

/**
* @author czs
* @version 创建时间：2018年6月15日 下午4:03:27<br>
* 用户登陆拦截器 
*/
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "YLF_USER_COOKIE");
		// 如果token为空，那肯定就是未登陆状态，
		if (StringUtils.isBlank(token)) {
			// 就跳转到sso的登陆界面，登陆成功后需要跳转到当前请求的Url
			response.sendRedirect(redirectUrl(request));
			// 拦截，不放行
			return false;
		}
		// 如果token存在，调用sso的系统服务，根据token取用户信息
		ylfResult result = tokenService.getUserByToken(token);
		// 如果取不到用户登陆已经过期，需要登陆
		if (result.getStatus() != 200) {
			// 就跳转到sso的登陆界面，登陆成功后需要跳转到当前请求的Url
			response.sendRedirect(redirectUrl(request));
			// 拦截，不放行
			return false;
		}
		// 如果取到用户信息，是登陆状态，需要把用户信息写入request
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车，如果有就要合并
		String cookieCart = CookieUtils.getCookieValue(request, "YLF_CAER_COOKIENAME", true);
		// if (cookieCart != null) {
		if (StringUtils.isNotBlank(cookieCart)) {
			cartService.mergeCart(user.getId(), JackJsonUtils.jsonToList(cookieCart, TbItem.class));
		}
		// 放行
		return true;
	}

	/**
	 * 获取重定向的地址，旨在没有登陆的情况下进入了订单系统<br>
	 * 所以要重定向到登陆系统，登陆完后需要重新回到这个url
	 * @param request 
	 * @return 计算后的重定向地址
	 */
	private String redirectUrl(HttpServletRequest request) {
		StringBuffer currentUrl = request.getRequestURL();// getRequestURL返回的是完整的url，包括Http协议，端口号，servlet名字和映射路径，但它不包含请求参数。
		String substring = currentUrl.substring(0, currentUrl.lastIndexOf(":"));
		String dlUrl = substring + ":8090/page/login";
		String redirectUrl = dlUrl + "?redirectSrc=" + currentUrl.toString();
		return redirectUrl;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
}
