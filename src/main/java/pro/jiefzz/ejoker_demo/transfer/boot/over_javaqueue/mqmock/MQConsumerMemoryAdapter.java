package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import pro.jiefzz.ejoker.common.system.enhance.MapUtil;
import pro.jiefzz.ejoker.common.system.functional.IVoidFunction2;
import pro.jiefzz.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jiefzz.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jiefzz.ejoker.queue.skeleton.aware.IConsumerWrokerAware;
import pro.jiefzz.ejoker.queue.skeleton.aware.IEJokerQueueMessageContext;

public class MQConsumerMemoryAdapter implements ICQProvider, IConsumerWrokerAware {

	private Queue<EJokerQueueMessage> queue = null;
	
	private IVoidFunction2<EJokerQueueMessage, IEJokerQueueMessageContext> vfHandler = null;
	
	private AtomicBoolean shutdownFlag = new AtomicBoolean(false);
	
	/// for debug
	private AtomicBoolean dispatchStatictics = new AtomicBoolean(false);
	private AtomicLong dispatchAmount = new AtomicLong(0l);
	private AtomicLong dispatchStartAt = new AtomicLong(-1l);
	private Queue<Long> dispatchLatestAll = new ConcurrentLinkedQueue<>();
	

	private AtomicBoolean sepOnce = new AtomicBoolean(false);
	private CountDownLatch blocker = new CountDownLatch(1);
	/// for debug end
	
	@Override
	public void start() throws Exception {
		new Thread(() -> {
			if(null == queue)
				throw new RuntimeException("queue is null!!!");
			int i = 0;
			for( ;; ) {
				EJokerQueueMessage msg = queue.poll();
				if(null != msg) {
					
					i = 0;
					
					/// for debug
					if(dispatchStartAt.compareAndSet(0l, 1l)) {
						dispatchStartAt.set(System.currentTimeMillis());
					};
					/// for debug end
					
					vfHandler.trigger(msg, m -> {
						
						/// for debug
						if(dispatchStatictics.get()) {
							dispatchAmount.incrementAndGet();
							dispatchLatestAll.offer(System.currentTimeMillis());
						}
						/// for debug end
						
					});
				} else {
					if(shutdownFlag.get())
						return;
					if(32 < ++ i ) {
						DiscardWrapper.sleepInterruptable(TimeUnit.MILLISECONDS, 1l);
						/// for debug
						if(sepOnce.get()) {
							try {
								blocker.await();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						/// for debug end
						i = 0;
					}
				}
			}
		}).start();
	}

	@Override
	public void shutdown() throws Exception {
		shutdownFlag.set(true);
	}

	@Override
	public void subscribe(String topic, String filter) {
		queue = MapUtil.getOrAdd(mockMsgQueues, topic, () -> new DSH()).queue;
	}

	@Override
	public void registerEJokerCallback(IVoidFunction2<EJokerQueueMessage, IEJokerQueueMessageContext> vf) {
		vfHandler = vf;
	}

	@Override
	public void loopInterval() {
		
	}


}
