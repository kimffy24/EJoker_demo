package pro.jiefzz.ejoker.test.rocketmq.pullConsumer;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {

        /* 
         * Instantiate with specified consumer group name. 
         */  
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");  
  
        /* 
         * Specify name server addresses. 
         * <p/> 
         * 
         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR 
         * <pre> 
         * {@code 
         * consumer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876"); 
         * } 
         * </pre> 
         */  
        consumer.setNamesrvAddr("192.168.199.123:9876");  
        /* 
         * Specify where to start in case the specified consumer group is a brand new one. 
         */  
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);  
  
        /* 
         * Subscribe one more more topics to consume. 
         */  
        consumer.subscribe("TopicTest", "*");  
  
        /* 
         *  Register callback to execute on arrival of messages fetched from brokers. 
         */  
        consumer.registerMessageListener(new MessageListenerConcurrently() {  
  
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,  
                                                            ConsumeConcurrentlyContext context) {  
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");  
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;  
            }  
        });  
  
        /* 
         *  Launch the consumer instance. 
         */  
        consumer.start();  
  
        System.out.printf("Consumer Started.%n");  
    }
}
