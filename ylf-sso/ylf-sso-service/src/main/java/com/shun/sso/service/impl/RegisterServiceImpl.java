package com.shun.sso.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.shun.common.pojo.ylfResult;
import com.shun.mapper.TbUserMapper;
import com.shun.pojo.TbUser;
import com.shun.pojo.TbUserExample;
import com.shun.pojo.TbUserExample.Criteria;
import com.shun.sso.service.RegisterService;

/**
* @author czs
* @version 创建时间：2018年6月7日 上午9:35:02 
*/
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;

	@Override
	public ylfResult checkUserData(String parameter, long type) {
		// 创建条件对象准备根据条件查询
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		// 如果是用户名的话
		if (type == 1) {
			criteria.andUsernameEqualTo(parameter);
		}
		// 如果是手机号的话
		else if (type == 2) {
			criteria.andPhoneEqualTo(parameter);
		}
		// 如果是邮箱的话
		else if (type == 3) {
			criteria.andEmailEqualTo(parameter);
		}
		// 如果不是1和2的话那么久返回错误的消息
		else {
			return ylfResult.build(400, "数据类型错误", null);
		}
		// 执行查询
		List<TbUser> users = userMapper.selectByExample(example);
		if (users != null && users.size() > 0) {
			return ylfResult.ok(false);
		}
		return ylfResult.ok(true);
	}

	@Override
	public ylfResult register(TbUser user) {
		// 判断是否为空
		if (user == null) {
			return ylfResult.build(400, "对象为空！出错，没能完成注册用户！");
		}
		if (user.getUsername() == null || StringUtils.isBlank(user.getUsername())) {
			return ylfResult.build(400, "用户名不能为空！，没能完成注册用户！");
		}
		if (user.getPhone() == null || StringUtils.isBlank(user.getPhone())) {
			return ylfResult.build(400, "手机号不能为空！，没能完成注册用户！");
		}
//		if (user.getEmail() == null || StringUtils.isBlank(user.getEmail())) {
//			return ylfResult.build(400, "邮箱不能为空！，没能完成注册用户！");
//		}
		// 判断用户名电话邮箱是否已经占用过了，如果占用了那就是返回的false
		if (!(boolean)checkUserData(user.getUsername(), 1).getData()) {
			return ylfResult.build(400, "用户名已占用！，没能完成注册用户！");
		}
		if (!(boolean)checkUserData(user.getPhone(), 2).getData()) {
			return ylfResult.build(400, "手机号已占用！，没能完成注册用户！");
		}
//		if (!(boolean)checkUserData(user.getEmail(), 3).getData()) {
//			return ylfResult.build(400, "邮箱已占用！，没能完成注册用户！");
//		}
		// 补全属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 加密密码
		String password = user.getPassword();
		String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
		user.setPassword(md5);
		// 开始插入数据库
		try {
			userMapper.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ylfResult.ok();
	}

}
