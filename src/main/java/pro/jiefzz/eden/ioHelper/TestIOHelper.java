package pro.jiefzz.eden.ioHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import pro.jiefzz.ejoker.eventing.EventAppendResult;
import pro.jiefzz.ejoker.z.io.IOHelper;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jiefzz.ejoker.z.task.AsyncTaskResult;
import pro.jiefzz.ejoker.z.task.AsyncTaskStatus;

public class TestIOHelper {

	public static void main(String[] args) {

		new TestIOHelper().start();
//		System.err.println("invoke LockSupport.park()!!! ");
		LockSupport.park();
	}
	
	IOHelper ioHelper = new IOHelper();

	public void start() {

		final List<String> testList = new ArrayList<String>();

		testList.add("金飞1");
		testList.add("龙轩1");
		testList.add("金飞2");
		testList.add("龙轩2");

		final AtomicInteger j = new AtomicInteger(0);
		final AtomicInteger i = new AtomicInteger(0);

		ioHelper.tryAsyncAction2(
				"测试的IOHelper异步任务",
				() -> {
					int cursor = i.get();
					AsyncTaskResult<EventAppendResult> actualTaskResult;

					if (cursor <= 5) {

						actualTaskResult = new AsyncTaskResult<>(AsyncTaskStatus.IOException,
								"模拟异步目标任务IO错误！");

						i.getAndIncrement();
					} else {

						System.out.println("***** 任务开始");
						System.out.println("testList.get()  ==> " + testList.get(j.get()));
						System.out.println("***** 任务完成");

						actualTaskResult = new AsyncTaskResult<EventAppendResult>(new EventAppendResult());

					}
					return EJokerFutureUtil.completeFuture(actualTaskResult); },
				actualResule -> {
					System.out.println("finishAction() was invoked!");

					System.out.println("  AsyncAction completed!");
					System.out.println("  Resulr status = ok");
					System.out.println("  cursor = " + i.get());

					System.out.println("=========== 标记一次异步任务完成 ==========");
					
					j.incrementAndGet();
					if(j.get() > 3)
						return;
					i.set(0);
					
				},
				() -> "testContextInfo",
				ex -> System.out.println("faildAction() was invoked! pass: " + ex.getMessage())
				);
	}
}
