package pro.jiefzz.eden.rocketmq.pullComsumer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class TestPullProductor {

	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("rmq-group-TestPullProductor");
		producer.setNamesrvAddr("192.168.199.123:9876");
		producer.setClientIP("192.168.199.52");
		producer.setInstanceName("rmq-instance");
		producer.start();
		System.out.println("ClientIP: " +producer.getClientIP());
		try {
			for (int i = 0; i < 198; i++) {
				String tag;
				if(0 == i%3)
					tag = "TagA";
				else if(1 == i%3)
					tag = "TagB";
				else
					tag = "TagC";
				Message msg = new Message("Topic-test-fuck", // topic
						tag, // tag
						("Hello RocketMQ ,QuickStart No." + i).getBytes()// body
				);
				SendResult sendResult = producer.send(msg);
				System.err.println(sendResult.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		producer.shutdown();
	}

}
