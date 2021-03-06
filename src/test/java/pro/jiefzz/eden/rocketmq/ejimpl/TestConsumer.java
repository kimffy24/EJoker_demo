package pro.jiefzz.eden.rocketmq.ejimpl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.consumer.MessageQueueListener;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import pro.jk.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jk.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jk.ejoker.queue.skeleton.aware.IConsumerWrokerAware;
import pro.jk.ejoker.queue.skeleton.aware.IEJokerQueueMessageContext;
import pro.jk.ejoker_support.mq.rocketmq.consumer.pull.DefaultMQConsumer;
import pro.jk.ejoker_support.rocketmq.DefaultMQConsumerEJokerBindder;

public class TestConsumer {

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		
		DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer("ZTestCGroup");
		defaultMQConsumer.setNamesrvAddr("test_rocketmq_2:9876;test_sit_1:9876");
		defaultMQConsumer.subscribe("ZTestTopic", null);

		IConsumerWrokerAware eJokerBundlePullConsumerDirectly = DefaultMQConsumerEJokerBindder.getEJokerBundlePullConsumerDirectly(defaultMQConsumer);
		eJokerBundlePullConsumerDirectly.registerEJokerCallback(TestConsumer::handle);
		
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
				DiscardWrapper.sleepInterruptable(TimeUnit.SECONDS, 2l);
				defaultMQConsumer.collectAndUpdateLocalOffset();
				System.err.println(".");
			}
		}).start();
		
	}
	
	public static void handle(EJokerQueueMessage queueMessage, IEJokerQueueMessageContext context) {
		
//		DefaultMQConsumer.EJokerQueueMessageContextImpl xcontext = (DefaultMQConsumer.EJokerQueueMessageContextImpl )context;
//		System.err.println(queueMessage.toString() + ". body: " + new String(queueMessage.getBody()) + ", queueId: " + xcontext.mq.getQueueId());
		context.onMessageHandled(queueMessage);
	}
	
}
