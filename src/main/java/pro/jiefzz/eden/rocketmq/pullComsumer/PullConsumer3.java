package pro.jiefzz.eden.rocketmq.pullComsumer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullConsumer3 {
	
	private final static Logger logger = LoggerFactory.getLogger(PullConsumer3.class);
	
	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("rmq-group-TestPullConsumer");
		consumer.setNamesrvAddr("192.168.199.123:9876");
		consumer.start();
		try {
			Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("Topic-test-fuck");
			for (MessageQueue mq : mqs) {
				System.out.println("Consume from the queue: " + mq);
				System.out.println("consumer.maxOffset(mq) = " + consumer.maxOffset(mq));
				System.out.println("consumer.minOffset(mq) = " + consumer.minOffset(mq));
				System.out.println("consumer.fetchConsumeOffset(mq, true) = " + consumer.fetchConsumeOffset(mq, true));
				System.err.println();
				new Thread(() -> {

					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					System.err.println("===================");
//					while(true) {
						try {
							PullResult pullResult = consumer.pull(mq, "TagC", 0, 32);
							
							switch(pullResult.getPullStatus()) {
							case FOUND:
								List<MessageExt> messageExtList = pullResult.getMsgFoundList();
								int msgSize = messageExtList.size();
								for (int i = 0; i < msgSize; i++) {
									System.out.println(String.format("  ==> msgOffset: %d, content: %s", i, new String(messageExtList.get(i).getBody())));
									
									consumer.updateConsumeOffset(mq, i);
									OffsetStore offsetStore = consumer.getOffsetStore();
									offsetStore.updateConsumeOffsetToBroker(mq, i, true);
								}
								break;
							default:
								logger.debug("{}", pullResult.getPullStatus());
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
//					}
				}).start();
			}

		} catch (MQClientException e) {
			e.printStackTrace();
		}

	}
}
