package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jiefzz.ejoker_demo.transfer.topicProviders.TopicReference;

/**
 * 清理掉测试时遗留在队列中的信息
 * @author kimffy
 *
 */
public class TransferTestLajiMessageConsumer2 {

	private final static Logger logger = LoggerFactory.getLogger(TransferTestLajiMessageConsumer2.class);
	
	public static void main(String[] args) throws Exception {

		String[][] tuples = new String [][] {
			new String[] {"EjokerDomainEventGroup", TopicReference.DomainEventTopic},
//			new String[] {"EjokerCommandGroup", TopicReference.CommandTopic},
//			new String[] {"EjokerApplicationMessageGroup", TopicReference.ApplicationMessageTopic},
//			new String[] {"EjokerDomainExceptionGroup", TopicReference.ExceptionTopic},
		};
		
		for(String[] tuple : tuples) {

			new Thread(() -> {
				try {
					xx(tuple[0], tuple[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		
		LockSupport.park();
	}

	public static void xx(String group, String topic) throws Exception {
		AtomicLong yy = new AtomicLong(0);
		DefaultMQPullConsumer c = new DefaultMQPullConsumer(group);
        c.setNamesrvAddr(Prepare.NameServAddr);
        c.start();
		Set<MessageQueue> fetchSubscribeMessageQueues = c.fetchSubscribeMessageQueues(topic);
		
		long currentOffset = 16496388;
		
		for(MessageQueue mq : fetchSubscribeMessageQueues) {
			try {
				PullResult pullResult = c.pullBlockIfNotFound(mq, null, currentOffset, 1);
				switch(pullResult.getPullStatus()) {
					case FOUND:
						List<MessageExt> messageExtList = pullResult.getMsgFoundList();
						MessageExt rmqMsg = messageExtList.get(0);
						EJokerQueueMessage eJokerQueueMessage = new EJokerQueueMessage(rmqMsg.getTopic(), rmqMsg.getFlag(), rmqMsg.getBody(), rmqMsg.getTags());
						
						System.err.println("rmqMsg.getTopic(): " + rmqMsg.getTopic());
						System.err.println("rmqMsg.getFlag(): " + rmqMsg.getFlag());
						System.err.println("rmqMsg.getBody(): " + new String(rmqMsg.getBody()));
						System.err.println("rmqMsg.getTags(): " + rmqMsg.getTags());
						
						break;
					case NO_MATCHED_MSG:
						logger.debug("[state: NO_MATCHED_MSG, topic: {}, queueId: {}]", mq.getTopic(), mq.getQueueId());
						break;
					case NO_NEW_MSG:
						logger.debug("[state: NO_NEW_MSG, topic: {}, queueId: {}]", mq.getTopic(), mq.getQueueId());
						break;
					case OFFSET_ILLEGAL:
						logger.warn("[state: OFFSET_ILLEGAL, queue: {}, offsetFetchLocal: {}, pullResult.getNextBeginOffset(): {}]", mq.toString(), currentOffset, pullResult.getNextBeginOffset());
					default:
						assert false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.err.println(String.format("%s: %d", topic, yy.get()));
	}
}
