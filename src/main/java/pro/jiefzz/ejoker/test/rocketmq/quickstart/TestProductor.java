package pro.jiefzz.ejoker.test.rocketmq.quickstart;

import java.util.Date;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class TestProductor {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("rmq-group");
		producer.setNamesrvAddr("192.168.1.232:9876");
		producer.setClientIP("192.168.1.36");
		producer.setInstanceName("rmq-instance");
		producer.start();
		System.out.println("ClientIP: " +producer.getClientIP());
		try {
			for (int i = 0; i < 3; i++) {
				Message msg = new Message("TopicA-test", // topic
						"TagA", // tag
						(new Date() + "Hello RocketMQ ,QuickStart" + i).getBytes()// body
				);
				/*SendResult sendResult = */producer.send(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		producer.shutdown();
	}

}
