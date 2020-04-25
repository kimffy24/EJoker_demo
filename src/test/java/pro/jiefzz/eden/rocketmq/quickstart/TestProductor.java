package pro.jiefzz.eden.rocketmq.quickstart;

import java.util.Date;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class TestProductor {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("rmq-group-p");

		producer.setNamesrvAddr("test_sit_1:9876;test_rocketmq_2:9876");
		producer.setClientIP("192.168.199.163");
		producer.setInstanceName("rmq-instance-p");
        
		producer.start();
		System.out.println("ClientIP: " +producer.getClientIP());
		try {
			for (int i = 0; i < 3; i++) {
				Message msg = new Message("ZTopicA-test", // topic
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
