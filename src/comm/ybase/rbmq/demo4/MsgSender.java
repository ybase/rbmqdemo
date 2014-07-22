package comm.ybase.rbmq.demo4;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgSender {

	private static final String EXCHANGE_NAME = "topic_msg2";
	private static final String QUEUE_NAME = "topic_queue2";

	public static void main(String args[]) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		cf.setVirtualHost("test_host");
		cf.setUsername("test");
		cf.setPassword("test");
		Connection conn = cf.newConnection();
		Channel nel = conn.createChannel();

		nel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, null);
		nel.queueDeclare(QUEUE_NAME, true, false, false, null);
		// nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.red.*");
		nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "green");
		// nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "black.*");

		// nel.basicPublish(EXCHANGE_NAME, "apple is red !", null,
		// "apple is red !".getBytes());
		// nel.basicPublish(EXCHANGE_NAME, "Color include green", null,
		// "Color include green".getBytes());
		// nel.basicPublish(EXCHANGE_NAME, "black is a color!", null,
		// "black is a color!".getBytes());

		BasicProperties prop = new BasicProperties();
		prop.setDeliveryMode(1); // 若等于1 ，则表示 消息服务重启，消息将丢失；等于2
									// 表示，若消息服务重启，消息将被持久化
		prop.setContentType("text/plain");
		// MessageProperties.PERSISTENT_TEXT_PLAIN 新方法代替prop

		nel.basicPublish(EXCHANGE_NAME, "green", MessageProperties.PERSISTENT_TEXT_PLAIN, "green msg 7!".getBytes());

		nel.close();
		conn.close();
	}
}
