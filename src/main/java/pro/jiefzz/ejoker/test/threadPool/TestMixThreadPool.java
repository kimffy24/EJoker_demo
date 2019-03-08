package pro.jiefzz.ejoker.test.threadPool;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.jiefzz.ejoker.EJokerEnvironment;
import com.jiefzz.ejoker.z.common.system.wrapper.MixedThreadPoolExecutor;

public class TestMixThreadPool {

	
	public static void main(String[] args) throws Exception {
		

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

				});
		
		Future<Object> handle = threadPoolExecutor.submit(() -> { throw new IOException("sb"); });
		
		
		handle.get();
		
		
		threadPoolExecutor.shutdown();
		
	}
}
