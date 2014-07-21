package comm.ybase.rbmq.demo1;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class EndPoint {

	protected Channel channel;
	protected Connection conn;
	protected String endPointName;

	public EndPoint(String endPointName) throws IOException {
		super();
		this.endPointName = endPointName;
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");
		conn = cf.newConnection();
		channel = conn.createChannel();
		channel.queueDeclare(endPointName, false, false, false, null);
	}

	/**
	 * Channel 和 Connection 的关闭都是隐式调用的<br/>
	 */
	public void close() throws IOException {
		this.channel.close();
		this.conn.close();
	}

}
