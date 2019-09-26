package pro.jiefzz.eden.rocketmq.utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

public class OffsetMonitor {

	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("EjokerCommandConsumerGroup");
		consumer.setNamesrvAddr("192.168.199.94:9876;192.168.199.123:9876");
		consumer.start();
		
		String[] topics = new String[] {"TopicEJokerCommand", "TopicDomainEvent"};


		for(String topic : topics) {
			Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(topic);
			for (MessageQueue mq : messageQueues) {
				System.err.println(mq);
				System.err.println("consumer.minOffset(mq) = " + consumer.minOffset(mq));
				System.err.println("consumer.maxOffset(mq) = " + consumer.maxOffset(mq));
				System.err.println("consumer.fetchConsumeOffset(mq, false) = " + consumer.fetchConsumeOffset(mq, false));
				System.err.println("========================\n");
			}
			System.err.println("");
			System.err.println("");
			TimeUnit.SECONDS.sleep(1);
		}

		consumer.shutdown();
			
	}

}
