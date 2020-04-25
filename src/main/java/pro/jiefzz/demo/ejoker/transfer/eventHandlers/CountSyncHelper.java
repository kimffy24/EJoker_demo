package pro.jiefzz.demo.ejoker.transfer.eventHandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;
import pro.jk.ejoker.common.system.extension.AsyncWrapperException;
import pro.jk.ejoker.common.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jk.ejoker.common.system.wrapper.CountDownLatchWrapper;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

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
		} catch (AsyncWrapperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionCompletedEvent message) {
		int currentCount = this.currentCount.incrementAndGet();
		if (currentCount == expectedCount) {
			CountDownLatchWrapper.countDown(waitHandle);
		}
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(DepositTransactionCompletedEvent message) {
		if(!markBefore.contains(message.getAggregateRootId())) {
			markBefore.add(message.getAggregateRootId());
			int currentCount = this.currentCount.incrementAndGet();
			if (currentCount == expectedCount) {
				CountDownLatchWrapper.countDown(waitHandle);
			}
		}
		return EJokerFutureUtil.completeFuture();
	}
}
