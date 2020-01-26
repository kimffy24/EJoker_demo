package pro.jiefzz.eden.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import pro.jiefzz.ejoker.EJokerEnvironment;
import pro.jiefzz.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jiefzz.ejoker.common.system.wrapper.MixedThreadPoolExecutor;

public class TestMixThreadPool {

	
	private final static AtomicInteger activeCounter = new AtomicInteger(0);
	
	public static void main(String[] args) {
		

		ThreadPoolExecutor threadPoolExecutor = new MixedThreadPoolExecutor(
				EJokerEnvironment.ASYNC_EJOKER_MESSAGE_SENDER_THREADPOLL_SIZE,
				EJokerEnvironment.ASYNC_EJOKER_MESSAGE_SENDER_THREADPOLL_SIZE, 0l, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {

					private final AtomicInteger threadIndex = new AtomicInteger(0);

					private final ThreadGroup group;

					private final String namePrefix;

					{

						SecurityManager s = System.getSecurityManager();
						group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
						namePrefix = "EJokerMongoPersists-thread-";
					}

					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(null, r, namePrefix + threadIndex.getAndIncrement(), 0);
						if (t.isDaemon())
							t.setDaemon(false);
						if (t.getPriority() != Thread.NORM_PRIORITY)
							t.setPriority(Thread.NORM_PRIORITY);

						return t;
					}

				}) {


			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				activeCounter.incrementAndGet();
				super.beforeExecute(t, r);
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				activeCounter.decrementAndGet();
			}
			
		};
//		
//		Future<Object> handle = threadPoolExecutor.submit(() -> { throw new IOException("sb"); });
//		
//		
//		
//		try {
//			handle.get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		

		threadPoolExecutor.submit(() -> { DiscardWrapper.sleepInterruptable(5000l); });
		threadPoolExecutor.submit(() -> { DiscardWrapper.sleepInterruptable(5000l); });
		threadPoolExecutor.submit(() -> { DiscardWrapper.sleepInterruptable(5000l); });
		
		while(true) {
			
			System.out.println(activeCounter.get());
			
			if(0==activeCounter.get())
			break;
		}
		
		threadPoolExecutor.shutdownNow();
		
	}
}
