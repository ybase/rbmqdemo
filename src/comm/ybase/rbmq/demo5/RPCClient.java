package comm.ybase.rbmq.demo5;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RPCClient {

	private Connection connection;
	private Channel channel;
	private String requestQueueName = "rpc_queue";
	private String replyQueueName;
	private QueueingConsumer consumer;

	public RPCClient() throws IOException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		cf.setVirtualHost("test_host");
		cf.setUsername("test");
		cf.setPassword("test");
		connection = cf.newConnection();
		channel = connection.createChannel();
		replyQueueName = channel.queueDeclare().getQueue();
		consumer = new QueueingConsumer(channel);
		channel.basicConsume(replyQueueName, true, consumer);
	}

	public String call(String message) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		String response = null;
		String corrId = UUID.randomUUID().toString();
		BasicProperties prop = new BasicProperties();
		prop.setReplyTo(replyQueueName);
		prop.setCorrelationId(corrId);
		channel.basicPublish("", requestQueueName, prop, message.getBytes());

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				response = new String(delivery.getBody(), "utf-8");
				break;
			}
		}
		return response;
	}

	public void close() throws IOException {
		connection.close();
	}

	public static void main(String args[]) throws Exception {
		RPCClient client = new RPCClient();
		String response = client.call("20");
		System.out.println("[x] Requesting fib(20) result[" + response + "]");
		response = client.call("45");
		System.out.println("[x] Requesting fib(45) result[" + response + "]");
		response = client.call("22");
		System.out.println("[x] Requesting fib(22) result[" + response + "]");
		if (client != null) {
			client.close();
		}
	}
}
