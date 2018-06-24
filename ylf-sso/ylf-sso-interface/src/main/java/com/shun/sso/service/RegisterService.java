package com.shun.sso.service;

import com.shun.common.pojo.ylfResult;
import com.shun.pojo.TbUser;

/**
* @author czs
* @version 创建时间：2018年6月7日 上午9:22:55 <br>
* 负责商城用户注册
*/
public interface RegisterService {
	
	/**
	 * 检查用户名手机是否存在
	 * @param parameter 用户名手机或者邮箱的字符串
	 * @param type 类型，如果是1的话那么就是用户名，如果是2的话就是手机号
	 * @return 如果占用了，那么返回值的data属性将会是false
	 */
	ylfResult checkUserData(String parameter, long type);
	
	/**
	 * 根据Controller层传过来的pojo对象去数据库中注册用户
	 * @param user 用户的pojo对象
	 * @return
	 */
	ylfResult register(TbUser user);
}
