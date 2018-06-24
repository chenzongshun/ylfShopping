package com.shun.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
* @author czs
* @version 创建时间：2018年6月1日 下午2:43:36<br>
* 这个类转专门用来实现MessageListener接口来接收消息
*/
public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// 打印结果
		TextMessage textMessage = (TextMessage) message;
		String text;
		try {
			text = textMessage.getText();
			System.out.println("Spring测试接收到消息，消息内容为：" + text + " 按任意键结束接收");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
