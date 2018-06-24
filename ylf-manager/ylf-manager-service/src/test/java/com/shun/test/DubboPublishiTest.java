package com.shun.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @author czs
* @version 创建时间：2018年5月19日 上午11:08:35 
*/
public class DubboPublishiTest {
	
	/**
	 * 不依赖Tomcat发布Dubbo的服务测试
	 * @throws Exception
	 */
	// @Test		// 注释掉，不让manager工程启动再次重新初始化spring工厂
	@SuppressWarnings("resource")
	public void dubboPublishi() throws Exception {
		new ClassPathXmlApplicationContext("/spring/applicationContext-*.xml");
		System.in.read();			// 等待控制台输入停止
	}
}
 