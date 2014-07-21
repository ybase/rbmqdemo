package comm.ybase.rbmq.demo2;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";
	private static final String QUEUE_NAME = "log_queue";

	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		Connection conn = cf.newConnection();
		Channel nel = conn.createChannel();
		nel.exchangeDeclare(EXCHANGE_NAME, "direct");

		QueueingConsumer qc = new QueueingConsumer(nel);
		nel.basicConsume(QUEUE_NAME, true, qc);
		while (true) {
			QueueingConsumer.Delivery delivery = qc.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();
			System.out.println("[X] Received RoutingKey[" + routingKey + "], message[]" + message);
		}
	}

}
