package com.shun.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.shun.common.pojo.ylfResult;
import com.shun.common.utils.JackJsonUtils;
import com.shun.sso.service.TokenService;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午10:32:12<br>
* 直接调用service根据token查询用户信息
*/
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;

	/*
	 * 原版不动的，没有使用jsonp之前的方法
	 * 
	 * @ResponseBody
	 * @RequestMapping("/user/token/{token}") 
	 * public ylfResult getUserByToken(@PathVariable String token) { 
	 * 		return tokenService.getUserByToken(token); 
	 * }
	 */

/**
 * 改进配合js的跨域ajax请求，老版本spring4.2以前的用设置返回值类型application/json来达到目的<br>
 * 客户端发送的跨域请求为http://localhost:8090/user/token/6cb7514c-6efc-4c77-8a17-dc524f92dbcc?callback=jsonp1528434107961
 * @param token 客户端cookie中发送过来的用户令牌
 * @param callback jquery提供的jsonp请求参数名，这个参数不能改！改必错~~~
 * @return 返回一条json结果字符串，将字符串放到callback{}的大括号中并返回<br>
 * 如：jsonp1528434107961({"status":200,"msg":"OK","data":{"id":1,"username":"zhangsan","password":null,"phone":"13488888888","email":"aa@a.cn","created":1428339835000,"updated":1428339835000}});
 */
@ResponseBody
// @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE/* "application/json;charset=utf-8" */)
public String getUserByTokenOld(@PathVariable String token, String callback) {
	ylfResult result = tokenService.getUserByToken(token);
	// 响应结果之前，判断是否为jsonp请求
	if (StringUtils.isNotBlank(callback)) {
		// 把结果封装成一个js语句响应
		return callback + "(" + JackJsonUtils.objectToJson(result) + ");";
		// jsonp1528434107961({"status":200,"msg":"OK","data":{"id":1,"username":"zhangsan","password":null,"phone":"13488888888","email":"aa@a.cn","created":1428339835000,"updated":1428339835000}});
	}
	return JackJsonUtils.objectToJson(result);
}

/**
 * 改进配合js的跨域ajax请求，新版本spring4.2以后的直接返回MappingJacksonValue对象就好<br>
 * @param token token 客户端cookie中发送过来的用户令牌
 * @param callback callback jquery提供的jsonp请求参数名，这个参数不能改！改必错~~~
 * @return 一个Object，如果带了callback就返回MappingJacksonValue，如果不带就直接返回对象
 */
@ResponseBody
@RequestMapping(value = "/user/token/{token}")
public Object getUserByTokenNew(@PathVariable String token, String callback) {
	ylfResult result = tokenService.getUserByToken(token);
	// 响应结果之前，判断是否为jsonp请求
	if (StringUtils.isNotBlank(callback)) {
		// 把结果封装成一个js语句响应
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
	return result;
}
}
