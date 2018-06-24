package com.shun.sso.service;

import com.shun.common.pojo.ylfResult;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午3:11:34<br>
* 负责商城用户登陆 
*/
public interface LoginService {

	/**
	 * 登陆功能
	 * @param username 用户名
	 * @param password 密码
	 * @return 如果返回的对象状态码为200，data是token(UUID)字符串的话说明登陆成功了，这个UUID应该存入cookie作为去redis取值的键
	 */
	ylfResult login(String username, String password);
}
