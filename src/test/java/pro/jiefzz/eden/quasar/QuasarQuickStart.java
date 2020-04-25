package pro.jiefzz.eden.quasar;
//package pro.jiefzz.ejoker.test.quasar;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.LongAdder;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
//import com.jiefzz.ejoker.z.common.task.context.AbstractNormalWorkerGroupService;
//import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;
//import com.jiefzz.ejoker_quasar.QuasarFiberExector;
//
//import co.paralleluniverse.fibers.Fiber;
//import co.paralleluniverse.fibers.SuspendExecution;
//import co.paralleluniverse.fibers.Suspendable;
//import co.paralleluniverse.strands.Strand;
//import co.paralleluniverse.strands.SuspendableCallable;
//
//public class QuasarQuickStart {
//
//	@Suspendable
//	static void m1() throws InterruptedException, SuspendExecution {
//		String m = "m1";
//		// System.out.println("m1 begin");
//		m = m2();
//		// System.out.println("m1 end");
//		// System.out.println(m);
//	}
//
//	static String m2() throws SuspendExecution, InterruptedException {
//		String m = m3();
//		Strand.sleep(1000l);
//		return m;
//	}
//
//	// or define in META-INF/suspendables
//	@Suspendable
//	static String m3() {
//		List l = Stream.of(1, 2, 3).filter(i -> i % 2 == 0).collect(Collectors.toList());
//		return l.toString();
//	}
//
//	static public void main(String[] args) throws ExecutionException, InterruptedException {
//		int count = 10000;
//		testThreadpool(count);
//		testFiber(count);
//		
//		new Thread().start();
//	}
//
//	static void testThreadpool(int count) throws InterruptedException {
//		final CountDownLatch latch = new CountDownLatch(count);
//		ExecutorService es = Executors.newFixedThreadPool(200);
//		LongAdder latency = new LongAdder();
//		long t = System.currentTimeMillis();
//		for (int i = 0; i < count; i++) {
//			es.submit(() -> {
//				long start = System.currentTimeMillis();
//				try {
//					m1();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (SuspendExecution suspendExecution) {
//					suspendExecution.printStackTrace();
//				}
//				start = System.currentTimeMillis() - start;
//				latency.add(start);
//				latch.countDown();
//			});
//		}
//		latch.await();
//		t = System.currentTimeMillis() - t;
//		long l = latency.longValue() / count;
//		System.out.println("thread pool took: " + t + ", latency: " + l + " ms");
//		es.shutdownNow();
//	}
//
//	static void testFiber(int count) throws InterruptedException {
//		
//		AbstractNormalWorkerGroupService.setAsyncEntranceProvider(QuasarFiberExector::new);
//		SystemAsyncHelper systemAsyncHelper = new SystemAsyncHelper();
//		
//		Method method;
//		try {
//			method = AbstractNormalWorkerGroupService.class.getDeclaredMethod("init");
//			method.setAccessible(true);
//			method.invoke(systemAsyncHelper);
//		} catch (NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//		
//		final CountDownLatch latch = new CountDownLatch(count);
//		LongAdder latency = new LongAdder();
//		long t = System.currentTimeMillis();
//		for (int i = 0; i < count; i++) {
//			systemAsyncHelper.submit(() -> {
////			Fiber<Void> fiber = new Fiber<Void>("Caller", () -> {
////					new Fiber<Void>(() -> {
//					SystemFutureWrapper<Void> submit = systemAsyncHelper.submit(() -> {
////					SystemFutureWrapper<Void> systemFutureWrapper = new SystemFutureWrapper<>(triggerx(() -> {
//								
//						long start = System.currentTimeMillis();
//						m1();
//						start = System.currentTimeMillis() - start;
//						latency.add(start);
//						latch.countDown();
//						return null;
//								
//					}); // submit.get();
////					}).start().get();
////					})); systemFutureWrapper.get();
//			});
//		}
//		latch.await();
//		t = System.currentTimeMillis() - t;
//		long l = latency.longValue() / count;
//		System.out.println("fiber took: " + t + ", latency: " + l + " ms");
//	}
//
//	private static <T> Fiber<T> triggerx(SuspendableCallable<T> sc) {
//
//		Fiber<T> fiber = new Fiber<T>(sc::run);
//		fiber.start();
//		return fiber;
//	}
//}
