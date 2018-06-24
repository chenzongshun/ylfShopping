package com.shun.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.common.utils.JackJsonUtils;
import com.shun.pojo.TbUser;
import com.shun.sso.service.TokenService;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午10:21:16<br>
* 根据token去redis数据库来取得用户对象
*/
@Service
public class TokenServiceImpl implements TokenService {

	@Value(value = "${SESSION_TOKEN_TIMEOUT}")
	private Integer SESSION_TOKEN_TIMEOUT;

	@Autowired
	private JedisClient jedisClient;

	@Override
	/**
	 * 根据token去redis数据库来取得用户对象
	 */
	public ylfResult getUserByToken(String token) {
		// 根据token去redis数据库取到user对象
		String userString = jedisClient.get("SESSION:" + token);
		// 如果为空说明过期或者没登录，直接返回
		if (StringUtils.isBlank(userString)) {
			return ylfResult.build(400, "可能未登录或者已超时，需要重新登录！");
		}
		// 如果找到了就根据返回的字符串转成pojo
		TbUser user = JackJsonUtils.jsonToPojo(userString, TbUser.class);
		// 重新设置过期时间
		jedisClient.expire("SESSION:" + token, SESSION_TOKEN_TIMEOUT);
		// 返回结果
		return ylfResult.ok(user);
	}

}
