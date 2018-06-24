package com.shun.test.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQQueue;
//import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.shun.common.utils.ThisProjectUtils;

public class Spring整合测试ActiveMq {

	//@Test
	/**
	 * 点对点发送方，这个是spring版的，注入了ActiveMQQueue以及JmsTemplate
	 */
	public void SPRING测试点对点发送方() {
		// 初始化spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activeMq.xml");
		// 从容器中获得JmsTemplate对象。
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		// 从容器中获得一个Destination对象。
		ActiveMQQueue activeMQQueue= applicationContext.getBean(ActiveMQQueue.class);
		activeMQQueue.setPhysicalName(ThisProjectUtils.getSpring测试点对点发送方的消息标题());
		// 发送消息
		jmsTemplate.send(activeMQQueue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send activemq message");
			}
		});
		System.out.println("消息发送完毕！");
	}
}
