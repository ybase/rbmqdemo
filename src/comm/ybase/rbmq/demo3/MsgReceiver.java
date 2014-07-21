package comm.ybase.rbmq.demo3;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgReceiver {

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

		QueueingConsumer qc = new QueueingConsumer(nel);
		nel.basicConsume(QUEUE_NAME, true, qc);
		while (true) {
			QueueingConsumer.Delivery delivery = qc.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();
			System.out.println("[X] Received RoutingKey[" + routingKey + "], message[" + message + "]");
		}
	}
}
