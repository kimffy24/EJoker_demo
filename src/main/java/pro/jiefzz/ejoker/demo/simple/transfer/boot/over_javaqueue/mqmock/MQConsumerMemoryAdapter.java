package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue.mqmock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jiefzz.ejoker.queue.aware.EJokerQueueMessage;
import com.jiefzz.ejoker.queue.aware.IConsumerWrokerAware;
import com.jiefzz.ejoker.queue.aware.IEJokerQueueMessageContext;
import com.jiefzz.ejoker.z.common.system.functional.IVoidFunction2;
import com.jiefzz.ejoker.z.common.system.helper.MapHelper;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

public class MQConsumerMemoryAdapter implements ICQProvider, IConsumerWrokerAware {

	private Queue<EJokerQueueMessage> queue = null;
	
	private IVoidFunction2<EJokerQueueMessage, IEJokerQueueMessageContext> vfHandler = null;
	
	private AtomicBoolean shutdownFlag = new AtomicBoolean(false);
	
	@Override
	public void start() throws Exception {
		IEJokerQueueMessageContext cxt = new IEJokerQueueMessageContext() {
			@Override
			public void onMessageHandled() {
			}} ;
		new Thread(() -> {
			while(!shutdownFlag.get()) {
				if(null == queue)
					throw new RuntimeException("queue is null!!!");
				boolean fullProcess = false;
				for(int i = 0; i < 32; i ++) {
					EJokerQueueMessage msg = queue.poll();
					if(null != msg) {
						vfHandler.trigger(msg, cxt);
						if(i == 31)
							fullProcess = true;
					}
				}
				if(!fullProcess)
					SleepWrapper.sleep(TimeUnit.MILLISECONDS, 2l);
			}
		}).start();
	}

	@Override
	public void shutdown() throws Exception {
		shutdownFlag.set(true);
	}

	@Override
	public void subscribe(String topic, String filter) {
		queue = MapHelper.getOrAddConcurrent(mockMsgQueues, topic, ConcurrentLinkedQueue::new);
	}

	@Override
	public void registerEJokerCallback(IVoidFunction2<EJokerQueueMessage, IEJokerQueueMessageContext> vf) {
		vfHandler = vf;
	}

	@Override
	public void syncOffsetToBroker() {
		
	}


}
