package comm.ybase.rbmq.demo2;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReceiveLogsDirect {

	private static final String EXCHANGE_NAME = "direct_logs";
	private static final String QUEUE_NAME = "log_queue";

	public static void main(String[] args) throws IOException {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		Connection conn = cf.newConnection();
		Channel nel = conn.createChannel();
		nel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String qn = nel.queueDeclare(QUEUE_NAME, true, false, false, null).getQueue();
		System.out.println("Queue Name[" + qn + "]");
		nel.queueBind(qn, EXCHANGE_NAME, "warn");
		nel.queueBind(qn, EXCHANGE_NAME, "info");
		nel.queueBind(qn, EXCHANGE_NAME, "error");

		nel.basicPublish(EXCHANGE_NAME, "info", null, "Hello Log3".getBytes());
		nel.close();
		conn.close();
	}

}
