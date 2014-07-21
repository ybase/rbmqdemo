package comm.ybase.rbmq.demo3;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgSender {

	private static final String EXCHANGE_NAME = "topic_msg";
	private static final String QUEUE_NAME = "topic_queue";

	public static void main(String args[]) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		cf.setVirtualHost("test_host");
		cf.setUsername("test");
		cf.setPassword("test");
		Connection conn = cf.newConnection();
		Channel nel = conn.createChannel();

		nel.exchangeDeclare(EXCHANGE_NAME, "topic", true, true, null);
		nel.queueDeclare(QUEUE_NAME, true, false, true, null);
		// nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.red.*");
		nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "green");
		nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.red");
		nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*bak");
		nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "#.one");
		// nel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "black.*");

		// nel.basicPublish(EXCHANGE_NAME, "apple is red !", null,
		// "apple is red !".getBytes());
		// nel.basicPublish(EXCHANGE_NAME, "Color include green", null,
		// "Color include green".getBytes());
		// nel.basicPublish(EXCHANGE_NAME, "black is a color!", null,
		// "black is a color!".getBytes());
		nel.basicPublish(EXCHANGE_NAME, "two.three.one", null, "Color include green!".getBytes());

		nel.close();
		conn.close();
	}
}
