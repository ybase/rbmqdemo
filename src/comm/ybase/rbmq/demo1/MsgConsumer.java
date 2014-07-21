package comm.ybase.rbmq.demo1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgConsumer extends EndPoint implements Runnable, Consumer {

	public MsgConsumer(String endPointName) throws IOException {
		super(endPointName);
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer[" + consumerTag + "] Registered!");
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		System.out.println("Consumer[" + consumerTag + "] Canceled!");
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		System.out.println("Consumer[" + consumerTag + "] Canceled [O]!");
	}

	@Override
	public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] arg3) throws IOException {
		Map map = (HashMap) SerializationUtils.deserialize(arg3);
		System.out.println("Message Number " + map.get("message number") + " received.");
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
	}

	@Override
	public void run() {
		try {
			channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
