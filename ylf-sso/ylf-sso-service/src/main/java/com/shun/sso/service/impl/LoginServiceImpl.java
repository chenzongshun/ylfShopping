package com.shun.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.shun.common.pojo.ylfResult;
import com.shun.common.redis.JedisClient;
import com.shun.common.utils.JackJsonUtils;
import com.shun.mapper.TbUserMapper;
import com.shun.pojo.TbUser;
import com.shun.pojo.TbUserExample;
import com.shun.pojo.TbUserExample.Criteria;
import com.shun.sso.service.LoginService;

/**
* @author czs
* @version 创建时间：2018年6月7日 下午3:14:54 
*/
@Service
public class LoginServiceImpl implements LoginService {

	@Value(value = "${SESSION_TOKEN_TIMEOUT}")
	private Integer SESSION_TOKEN_TIMEOUT;

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisclient;

	@Override
	public ylfResult login(String username, String password) {
		String fail = "用户名或密码错误！";
		// 判断用户名是否为空
		if (StringUtils.isBlank(username)) {
			return ylfResult.build(400, fail);
		}
		// 根据用户名查询用户对象
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> users = userMapper.selectByExample(example);
		if (users == null || users.size() == 0) {
			return ylfResult.build(400, fail);
		}
		// 用MD5加密密码然后对比用户对象的密码是否正确
		TbUser user = users.get(0);
		String md5pwd = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!user.getPassword().equals(md5pwd)) {
			return ylfResult.build(400, fail);
		}
		// 生成UUID作为token作为令牌页
		String token = UUID.randomUUID().toString();
		// 用令牌作为redis键，键值为用户对象存入redis，记得清空密码
		// 因为将来判断登录的时候会去redis这个假session里面将user查出来，那么也会包含密码，有安全隐患
		user.setPassword(null);
		jedisclient.set("SESSION:" + token, JackJsonUtils.objectToJson(user));
		// 别忘了设置过期时间
		jedisclient.expire("SESSION:" + token, SESSION_TOKEN_TIMEOUT);
		// 将token存入cookie中，由于写cookie需要response，业务层只写逻辑所以是写不了了，所以直接返回
		return ylfResult.ok(token);
	}

}
