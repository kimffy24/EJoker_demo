package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.AbstractEJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.TopicReference;

/**
 * 清理掉测试时遗留在队列中的信息
 * @author kimffy
 *
 */
public class TransferTestLajiMessageConsumer {

	public static void main(String[] args) throws Exception {
		
		String[][] tuples = new String [][] {
			new String[] {AbstractEJokerBootstrap.EJokerDomainEventGroup, TopicReference.DomainEventTopic},
			new String[] {AbstractEJokerBootstrap.EJokerCommandGroup, TopicReference.CommandTopic},
			new String[] {AbstractEJokerBootstrap.EJokerApplicationMessageGroup, TopicReference.ApplicationMessageTopic},
			new String[] {AbstractEJokerBootstrap.EJokerPublishableExceptionGroup, TopicReference.ExceptionTopic},
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
		
	}

	public static void xx(String group, String topic) throws Exception {
		AtomicLong yy = new AtomicLong(0);
		DefaultMQPullConsumer c = new DefaultMQPullConsumer(group);
        c.setNamesrvAddr(EJokerBootstrap.NameServAddr);
        c.start();
		Set<MessageQueue> fetchSubscribeMessageQueues = c.fetchSubscribeMessageQueues(topic);
		
		for(MessageQueue mq : fetchSubscribeMessageQueues) {
			long maxOffset = c.maxOffset(mq);
			yy.addAndGet(maxOffset);
			c.updateConsumeOffset(mq, maxOffset);
		}
		
		c.getOffsetStore().persistAll(fetchSubscribeMessageQueues);
		System.err.println(String.format("%s: %d", topic, yy.get()));
		c.shutdown();
	}
}
