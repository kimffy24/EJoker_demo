package pro.jiefzz.eden.rocketmq.pullComsumer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import co.paralleluniverse.common.util.Tuple;
import pro.jk.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jk.ejoker.queue.skeleton.aware.IEJokerQueueMessageContext;
import pro.jk.ejoker_support.rocketmq.DefaultMQConsumer;

public class PullConsumer2 {
	
	public final static Queue<Tuple> contextQueue = new ConcurrentLinkedQueue<>();
	
	public static void main(String[] args) throws Exception {
		DefaultMQConsumer consumer = new DefaultMQConsumer("pullConsumer");
		consumer.setNamesrvAddr("192.168.199.123:9876");
		
		consumer.registerEJokerCallback((msg, context) -> {
			
			System.out.println("Received: " + new String(msg.getBody()));
			
			contextQueue.offer(new Tuple(context, msg));
			
		});
		
		consumer.subscribe("TopicTest", "*");
		
		consumer.start();
		
		new Thread(() -> {
			for( ;; ) {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
//				consumer.showOffsetInfo();
			}
		}).start();

		new Thread(() -> {
			for( ;; ) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
				consumer.loopInterval();
			}
		}).start();

		new Thread(() -> {
			
			for( ;; ) {

				try {
					TimeUnit.MILLISECONDS.sleep(System.currentTimeMillis()%21);
				} catch (InterruptedException e) {
				}
				Tuple currentTuple = contextQueue.poll();
				if(null != currentTuple) {
					if(System.currentTimeMillis()%5 == 0)
						contextQueue.offer(currentTuple);
					else
						currentTuple.currentContext.onMessageHandled(null);
				}
			}
		}).start();
	}
	
	private final static class Tuple {
		
		public final IEJokerQueueMessageContext currentContext;
		
		public final EJokerQueueMessage message;

		public Tuple(IEJokerQueueMessageContext currentContext, EJokerQueueMessage message) {
			super();
			this.currentContext = currentContext;
			this.message = message;
		}
		
	}
}
