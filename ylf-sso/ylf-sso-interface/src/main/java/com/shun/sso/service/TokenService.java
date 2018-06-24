package com.shun.sso.service;

import com.shun.common.pojo.ylfResult;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午10:19:26<br>
* 根据token去redis数据库来取得用户对象
*/
public interface TokenService {

	/**
	 * 根据token去redis数据库来取得用户对象
	 * @param token
	 * @return 如果取到了就要给返回值对象赋值200的状态码成功
	 */
	ylfResult getUserByToken(String token);
}
