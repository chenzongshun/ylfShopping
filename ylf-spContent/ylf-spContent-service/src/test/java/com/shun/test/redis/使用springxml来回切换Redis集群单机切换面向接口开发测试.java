package com.shun.test.redis;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.shun.common.redis.JedisClient;

/**
* @author czs
* @version 创建时间：2018年5月24日 下午5:06:20 
* 无论是采用集群还是单机版的redis，在这里都可以进行运行，因为这个是面向接口开发，只需在xml中配置不同的实现类即可
*/
public class 使用springxml来回切换Redis集群单机切换面向接口开发测试 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
			JedisClient bean = context.getBean(JedisClient.class);
			bean.set("shun", "shun");
			System.err.println(bean.get("shun"));
		} catch (BeansException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
