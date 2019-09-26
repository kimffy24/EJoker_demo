package pro.jiefzz.eden.rocketmq.pullComsumer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

public class TOffsetMonitor {

	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pullConsumer");
		consumer.setNamesrvAddr("192.168.199.123:9876");
		consumer.start();

		Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("TopicTest");

		while(true) {
			for (MessageQueue mq : messageQueues) {
				System.err.println(mq);
				System.err.println("consumer.minOffset(mq) = " + consumer.minOffset(mq));
				System.err.println("consumer.maxOffset(mq) = " + consumer.maxOffset(mq));
				System.err.println("consumer.fetchConsumeOffset(mq, false) = " + consumer.fetchConsumeOffset(mq, false));
				System.err.println("========================\n");
			}
			System.err.println("");
			TimeUnit.SECONDS.sleep(1);
		}

	}

}
