package comm.ybase.rbmq.demo5;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCServer {
	private static final String RPC_QUEUE_NAME = "rpc_queue";

	private static int fib(int n) {
		if (n > 1)
			return fib(n - 1) + fib(n - 2);
		else
			return n;
	}

	public static void main(String args[]) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory cf = new ConnectionFactory();
			cf.setHost("localhost");
			cf.setVirtualHost("test_host");
			cf.setUsername("test");
			cf.setPassword("test");
			connection = cf.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(RPC_QUEUE_NAME, false, false, true, null);
			channel.basicQos(1);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

			while (true) {
				String response = null;
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				BasicProperties prop = delivery.getProperties();
				BasicProperties replyProps = new BasicProperties();
				replyProps.setCorrelationId(prop.getCorrelationId());

				try {
					String message = new String(delivery.getBody(), "utf-8");
					int n = Integer.parseInt(message);
					System.out.println(" [.] fib(" + message + ")");
					response = "" + fib(n);
				} catch (Exception e) {
					System.out.println(" [.] " + e.toString());
					response = "";
				} finally {
					channel.basicPublish("", prop.getReplyTo(), replyProps, response.getBytes("utf-8"));
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}
}
