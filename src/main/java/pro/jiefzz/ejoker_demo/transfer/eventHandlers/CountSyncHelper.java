package pro.jiefzz.ejoker_demo.transfer.eventHandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.MessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.AsyncWrapperException;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapper;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapperUtil;
import pro.jiefzz.ejoker.z.system.wrapper.CountDownLatchWrapper;
import pro.jiefzz.ejoker.z.task.AsyncTaskResult;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

// @MessageHandler
// @EService
public class CountSyncHelper extends AbstractMessageHandler {

	private Object waitHandle = CountDownLatchWrapper.newCountDownLatch();
	private int expectedCount;
	private AtomicInteger currentCount = new AtomicInteger(0);
	private Set<String> markBefore = new HashSet<>();

	public void setExpectedCount(int expectedCount) {
		this.expectedCount = expectedCount;
	}

	@Suspendable
	public void waitOne() {
		try {
			CountDownLatchWrapper.await(waitHandle);
		} catch (AsyncWrapperException e) {
			e.printStackTrace();
		}
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCompletedEvent message) {
		int currentCount = this.currentCount.incrementAndGet();
		if (currentCount == expectedCount) {
			CountDownLatchWrapper.countDown(waitHandle);
		}
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionCompletedEvent message) {
		if(!markBefore.contains(message.getAggregateRootId())) {
			markBefore.add(message.getAggregateRootId());
			int currentCount = this.currentCount.incrementAndGet();
			if (currentCount == expectedCount) {
				CountDownLatchWrapper.countDown(waitHandle);
			}
		}
		return SystemFutureWrapperUtil.completeFutureTask();
	}
}
