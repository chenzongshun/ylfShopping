package com.shun.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbUser;
import com.shun.sso.service.RegisterService;

/**
* @author czs
* @version 创建时间：2018年6月7日 上午9:04:07 
*/
@Controller
public class RegisterController {

	@Autowired
	private RegisterService regisService;

	/**
	 * 跳转到register界面
	 * @return
	 */
	@RequestMapping(value = "/page/register")
	public String showRegister() {
		return "register";
	}

	/**
	 * 检查用户名手机号或者邮箱是否为空
	 * @param parameter
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/user/check/{parameter}/{type}")
	public ylfResult checkUserData(@PathVariable String parameter, @PathVariable Integer type) {
		return  regisService.checkUserData(parameter, type);
	}

	
	@ResponseBody
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ylfResult register(TbUser user) {
		return regisService.register(user);
	}

}
