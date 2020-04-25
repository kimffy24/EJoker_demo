package pro.jiefzz.eden.rocketmq.quickstart;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class TestConsumer {

	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
                "rmq-group");

        consumer.setNamesrvAddr("test_sit_1:9876;test_rocketmq_2:9876");
        consumer.setClientIP("192.168.199.163");
        consumer.setInstanceName("rmq-instance");
        consumer.subscribe("ZTopicA-test", "TagA");

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
        consumer.start();
        
        System.out.println("ClientIp: " +consumer.getClientIP());
        
        System.out.println("Consumer Started.");
	}

}
