package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.system.extension.AsyncWrapperException;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.wrapper.CountDownLatchWrapper;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

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
