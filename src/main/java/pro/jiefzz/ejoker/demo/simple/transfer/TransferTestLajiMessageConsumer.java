package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.TopicReference;

/**
 * 清理掉测试时遗留在队列中的信息
 * @author kimffy
 *
 */
public class TransferTestLajiMessageConsumer {

	public static void main(String[] args) throws Exception {
		
		String[][] tuples = new String [][] {
			new String[] {EJokerBootstrap.EJokerDomainEventConsumerGroup, TopicReference.DomainEventTopic},
			new String[] {EJokerBootstrap.EJokerCommandConsumerGroup, TopicReference.CommandTopic},
			new String[] {EJokerBootstrap.EJokerApplicationMessageConsumerGroup, TopicReference.ApplicationMessageTopic},
			new String[] {EJokerBootstrap.EJokerPublishableExceptionConsumerGroup, TopicReference.ExceptionTopic},
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
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);

        consumer.setNamesrvAddr(EJokerBootstrap.NameServAddr);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println(new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        consumer.subscribe(topic, "*");
        consumer.start();
	}
}
