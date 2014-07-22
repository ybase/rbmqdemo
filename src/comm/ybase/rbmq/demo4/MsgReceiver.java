package comm.ybase.rbmq.demo4;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgReceiver {

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

		QueueingConsumer qc = new QueueingConsumer(nel);
		nel.basicConsume(QUEUE_NAME, false, qc);// autoAck false 表示只有当
												// 调用baiscAck
												// 方法之后，才会释放资源，告诉服务器消息被处理了
		while (true) {
			QueueingConsumer.Delivery delivery = qc.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();
			// nel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			//忘记ack是一个容易犯并且后果很严重的错误。RabbitMQ会侵占越来越多的内存，因为它不会释放没有被ack的消息。
			System.out.println("[X] Received RoutingKey[" + routingKey + "], message[" + message + "]");
		}
	}
}
