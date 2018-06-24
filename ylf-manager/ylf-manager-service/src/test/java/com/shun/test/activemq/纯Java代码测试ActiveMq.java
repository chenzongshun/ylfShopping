package com.shun.test.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;
//import org.junit.Test;

/**
 * 测试ActiveMq
 * @author 疙瘩陈
 *
 */
public class 纯Java代码测试ActiveMq {

	/**
	 * 服务器地址。
	 */
	private String remoteUrl = "tcp://47.93.253.48:61616";

	/**
	 * 点到点形式发送消息，pub(publish)发送方<br>
	 * 会创建一个test-queue的Queue队列，如果对方没有接收到，那么服务器端后台管理的Number Of Pending Messages待处理消息的数量就会++<br>
	 * 如果对方接收到了就会不变，因为对方接收到了嘛。
	 * @throws Exception
	 */
	//@Test
	public void 点对点发送方() throws Exception {
		// 1、创建一个连接工厂对象，需要指定服务的ip及端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(remoteUrl);
		// 2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 3、开启连接，调用Connection对象的start方法。
		connection.start();
		// 4、创建一个Session对象。
		// 第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
		// 第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
		// 自动应答：Session.AUTO_ACKNOWLEDGE手动应答：Session.CLIENT_ACKNOWLEDGE
		/**
		 * 这个事务不是数据库中的跟那个事务没关系，这个是activeMq中的事务，意思是说如果消息没有发出去的话就重发
		 * 这个事务是和数据库的分布式事务配合使用的，在同时提交多个数据库，保证多个事件都在一个事务里面完成。想想都头疼，所以一般很少有人用
		 * 所以一般不开启事务，如果开启事务，第二个参数无意义，自动忽略，如果false的话那么就是应答模式。
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用Session对象创建一个Destination对象，创建一个叫test-queue的队列信息。两种形式queue、topic，现在应该使用queue
		Queue queue = session.createQueue("test-queue");
		// 6、使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(queue);
		// 7、创建一个Message对象，可以使用TextMessage。
		/*
		 * TextMessage textMessage = new ActiveMQTextMessage();
		 * textMessage.setText("hello Activemq");
		 */
		TextMessage textMessage = session.createTextMessage("hello activemq");
		// 8、发送消息
		producer.send(textMessage);
		// 9、关闭资源
		producer.close();
		session.close();
		connection.close();
		System.out.println("消息发送成功！");
	}

	//@Test
	/**
	 * 点到点形式发送消息，sub(subscribe)接收方<br>
	 * 如果接收到了消息，那么服务器端后台管理的Number Of Pending Messages待处理消息的数量就会++
	 * 如果接收方一致不停止运行，那么消息发送方一旦在test-queue里面发送了消息的话，那么这边就会立马接收到消息<br>
	 * 那么服务器端后台管理的Number Of Pending Messages待处理消息的数量就不会变，因为接收端立马就处理了嘛。<br>
	 * 如果想关闭掉，那么久在这个接收方按下任意键就结束接收了。因为这里用到了system.in.rede();
	 * @throws Exception
	 */
	public void 点对点接收方() throws Exception {
		// 创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(remoteUrl);
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个Destination对象。queue对象，注意对列名一定要和发送方的对列名要一致！
		Queue queue = session.createQueue("test-queue");
		// 使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(queue);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println("接收到消息，消息内容为：" + text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		// 等待接收消息
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

	//@Test
	/**
	 * 广播与订阅方式，这里是广播方<br>
	 * 会创建一个test-topic的广播，不管有没有人关注接收广播，服务器端都不会进行保存，没有人接收就是没有人接收，广播方只负责广播<br>
	 * 这是和点对点不一样之处，只能看到Messages Dequeued(消息已出队)++<br>
	 * 其实下面的代码就是复制了点对点的发送方的，然后session.createQueue改成了session.createTopic而已
	 * @throws Exception
	 */
	public void 广播方() throws Exception {
		// 1、创建一个连接工厂对象，需要指定服务的ip及端口。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(remoteUrl);
		// 2、使用工厂对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 3、开启连接，调用Connection对象的start方法。
		connection.start();
		// 4、创建一个Session对象。
		// 第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
		// 第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5、使用Session对象创建一个Destination对象。两种形式queue、topic，现在应该使用topic
		Topic topic = session.createTopic("test-topic");
		// 6、使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(topic);
		// 7、创建一个Message对象，可以使用TextMessage。
		/*
		 * TextMessage textMessage = new ActiveMQTextMessage();
		 * textMessage.setText("hello Activemq");
		 */
		TextMessage textMessage = session.createTextMessage("topic message test");
		// 8、发送消息
		producer.send(textMessage);
		// 9、关闭资源
		producer.close();
		session.close();
		connection.close();
		System.out.println("广播成功");
	}

	//@Test
	/**
	 * 广播与订阅方式，这里是订阅方<br>
	 * 想要接收到订阅到的消息那么一定要静这个方法启动。如果不启动消息就会丢失<br>
	 * 在这里会启动三个方法，进行订阅，一旦广播方进行了广播，那么这里的三个方法都会接收到消息。
	 * @throws Exception
	 */
	public void 订阅方() throws Exception {
		// 创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(remoteUrl);
		// 创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个Destination对象。topic对象
		Topic topic = session.createTopic("test-topic");
		// 使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(topic);
		// 接收消息
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println("接收到订阅消息，消息内容为： " + text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic消费者3启动。。。。");
		// 等待接收消息
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
