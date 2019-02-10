package pro.jiefzz.ejoker.test.lajijava.threadPool1;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import com.jiefzz.ejoker.z.common.system.wrapper.MittenWrapper;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

public class MainThreadPool1 {

	public static void main(String[] args) throws Exception {
		new MainThreadPool1().start();
	}
	
	public void start() {
//		bridgeTurner.start();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				16,
				16,
				0l,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new SendThreadFactory()) {

					final Map<Future<?>, MittenWrapper> dict = new ConcurrentHashMap<>();
			
					@Override
					protected void afterExecute(Runnable r, Throwable t) {
						super.afterExecute(r, t);
						MittenWrapper mw;
						if(null != (mw = dict.remove(r))) {
							MittenWrapper.unpark(mw);
						}
						
					}

					@Override
					protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
						RunnableFuture<T> newTask = new FutureTask<>(runnable, value);
						dict.put(newTask, MittenWrapper.currentThread());
						return newTask;
					}

					@Override
					protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
						RunnableFuture<T> newTask = new FutureTask<>(callable);
						dict.put(newTask, MittenWrapper.currentThread());
						return newTask;
					}

			final class FutureTask<V> extends java.util.concurrent.FutureTask<V> {

				public FutureTask(Callable<V> callable) {
					super(callable);
				}

				public FutureTask(Runnable runnable, V result) {
					super(runnable, result);
				}

				@Override
				public V get() throws InterruptedException, ExecutionException {
					if(MittenWrapper.isCurrentFiber()) {
//						dict.
					}
					return super.get();
				}

				@Override
				public V get(long timeout, TimeUnit unit)
						throws InterruptedException, ExecutionException, TimeoutException {
					return super.get(timeout, unit);
				}
				
			}
		};
		threadPoolExecutor.prestartAllCoreThreads();
		
		for(int i = 0; i < 1; i++ )
			threadPoolExecutor.submit(()-> {

				System.err.println("child thread invoking sleep(1s)");
				SleepWrapper.sleep(TimeUnit.SECONDS, 1l);
				
			});

		System.err.println("main thread invoking park()");
		LockSupport.park();
		System.err.println("It work~");
		
		threadPoolExecutor.shutdown();
	}

	private final static class SendThreadFactory implements ThreadFactory {

		private final static AtomicInteger poolIndex = new AtomicInteger(0);

		private final AtomicInteger threadIndex = new AtomicInteger(0);

		private final ThreadGroup group;

		private final String namePrefix;

		public SendThreadFactory() {

			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "EJokerSender-" + poolIndex.incrementAndGet() + "-thread-";
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

	}

	
}
