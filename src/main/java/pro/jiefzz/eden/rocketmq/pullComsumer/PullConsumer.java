package pro.jiefzz.eden.rocketmq.pullComsumer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullCallback;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullConsumer {
	
	private final static Logger logger = LoggerFactory.getLogger(PullConsumer.class);
	
	public static void main(String[] args) throws Exception {
		DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pullConsumer");
		consumer.setNamesrvAddr("192.168.199.123:9876");
		consumer.start();
		final AtomicBoolean running = new AtomicBoolean(true);
		final int batchSize = 4;
		try {
			Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicTest");
			for (MessageQueue mq : mqs) {
				System.out.println("Consume from the queue: " + mq);
				System.out.println("consumer.maxOffset(mq) = " + consumer.maxOffset(mq));
				System.out.println("consumer.minOffset(mq) = " + consumer.minOffset(mq));
				System.out.println("consumer.fetchConsumeOffset(mq, true) = " + consumer.fetchConsumeOffset(mq, true));
				// long offset = consumer.fetchConsumeOffset(mq, true);
				// PullResultExt pullResult =(PullResultExt)consumer.pull(mq, null,
				// getMessageQueueOffset(mq), 32);
				// 消息未到达默认是阻塞10秒，private long consumerPullTimeoutMillis = 1000 * 10;
				new Thread(() -> {

					try {
						System.out.println("consumer.maxOffset(mq) = " + consumer.maxOffset(mq));
					} catch (MQClientException e2) {
						e2.printStackTrace();
					}
					
					long fetchConsumeOffset;
					try {
						fetchConsumeOffset = consumer.fetchConsumeOffset(mq, true);
						System.out.println("ConsumeOffset = " + fetchConsumeOffset);
					} catch (MQClientException e1) {
						throw new RuntimeException(e1);
					}
					
					final AtomicLong offset = new AtomicLong(fetchConsumeOffset);
					
					for( ; ; ) {
						try {
							long currentOffset = offset.get();
							try {
								for( ; consumer.maxOffset(mq) <= currentOffset + 1 ; ) {
									TimeUnit.SECONDS.sleep(1);
									System.out.println("Waiting!");
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
							
							System.out.println("current offset = " + currentOffset);
							consumer.pullBlockIfNotFound(mq, null, currentOffset, batchSize, new PullCallback() {
								@Override
								public void onSuccess(PullResult pullResult) {
									int msgSize = 0;
									switch (pullResult.getPullStatus()) {
									case FOUND:
										List<MessageExt> messageExtList = pullResult.getMsgFoundList();
										msgSize = messageExtList.size();
										System.out.println(" => found msg: " + msgSize);
										for (int i = 0; i < msgSize; i++) {
											System.out.println(String.format("  ==> msgOffset: %d, content: %s", pullResult.getNextBeginOffset()-4+i, new String(messageExtList.get(i).getBody())));
										}
										break;
									case NO_MATCHED_MSG:
										logger.debug("NO_MATCHED_MSG");
										break;
									case NO_NEW_MSG:
										logger.debug("NO_NEW_MSG");
										break;
									case OFFSET_ILLEGAL:
										logger.debug("OFFSET_ILLEGAL");
										break;
									default:
										logger.debug("default");
										break;
									}
									
									offset.getAndAdd(msgSize);
									try {
										consumer.updateConsumeOffset(mq, offset.get());
									} catch (MQClientException e) {
										e.printStackTrace();
									}
								}
	
								@Override
								public void onException(Throwable e) {
									
								}});
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

			new Thread(() ->  {
				while(true) {
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.err.println("Try presis all mq offset!!!");
					consumer.getOffsetStore().persistAll(mqs);
				}
			}).start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}

	}
}
