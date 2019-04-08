package pro.jiefzz.ejoker.test.rocketmq.ejimpl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.consumer.MessageQueueListener;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.jiefzz.ejoker.queue.aware.EJokerQueueMessage;
import com.jiefzz.ejoker.queue.aware.IEJokerQueueMessageContext;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

public class TestConsumer {

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		
		DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer("ZTestCGroup");
		defaultMQConsumer.setNamesrvAddr(EJokerBootstrap.NameServAddr);
		defaultMQConsumer.subscribe("ZTestTopic", null);

		defaultMQConsumer.registerEJokerCallback(TestConsumer::handle);
		
		defaultMQConsumer.registerMessageQueueListener("ZTestTopic", new MessageQueueListener() {

			@Override
			public void messageQueueChanged(String topic, Set<MessageQueue> mqAll, Set<MessageQueue> mqDivided) {
				System.err.println("change!!!");
				System.err.print("mqAll: ");
				System.err.println(mqAll);
				System.err.print("mqDivided: ");
				System.err.println(mqDivided);
			}
			
		});
		
		defaultMQConsumer.start();

		new Thread(() -> {
			while(true) {
				SleepWrapper.sleep(TimeUnit.SECONDS, 2l);
				defaultMQConsumer.syncOffsetToBroker();
				System.err.println(".");
			}
		}).start();
		
	}
	
	public static void handle(EJokerQueueMessage queueMessage, IEJokerQueueMessageContext context) {
		
//		DefaultMQConsumer.EJokerQueueMessageContextImpl xcontext = (DefaultMQConsumer.EJokerQueueMessageContextImpl )context;
//		System.err.println(queueMessage.toString() + ". body: " + new String(queueMessage.getBody()) + ", queueId: " + xcontext.mq.getQueueId());
		context.onMessageHandled();
	}
	
}
