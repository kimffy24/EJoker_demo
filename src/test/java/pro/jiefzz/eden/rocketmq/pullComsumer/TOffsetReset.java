package pro.jiefzz.eden.rocketmq.pullComsumer;

import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

public class TOffsetReset {

	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pullConsumer");
		consumer.setNamesrvAddr("192.168.199.123:9876");
		consumer.start();

		Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("TopicTest");

		for (MessageQueue mq : messageQueues) {
			consumer.getOffsetStore().updateConsumeOffsetToBroker(mq, 0, true);
		}

	}

}
