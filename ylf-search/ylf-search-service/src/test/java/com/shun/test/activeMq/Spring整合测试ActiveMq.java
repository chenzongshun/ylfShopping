package com.shun.test.activeMq;

import org.apache.activemq.command.ActiveMQQueue;
//import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.shun.common.utils.ThisProjectUtils;

public class Spring整合测试ActiveMq {

	@SuppressWarnings("resource")
//	@Test
	public void msgConsumer() throws Exception {
		//初始化spring容器
		ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activeMq.xml");
		ActiveMQQueue activeMQQueue = content.getBean(ActiveMQQueue.class);
		activeMQQueue.setPhysicalName(ThisProjectUtils.getSpring测试点对点发送方的消息标题());;
		//等待
		System.in.read();
	}
}