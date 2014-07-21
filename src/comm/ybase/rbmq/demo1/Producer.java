package comm.ybase.rbmq.demo1;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

public class Producer extends EndPoint {

	public Producer(String endPointName) throws IOException {
		super(endPointName);
	}

	public void sendMsg(Serializable obj) throws IOException {
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(obj)); //使用了默认exchange - 属于direct
	}

}
