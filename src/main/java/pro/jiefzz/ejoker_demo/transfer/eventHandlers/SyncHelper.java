package pro.jiefzz.ejoker_demo.transfer.eventHandlers;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.MessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapper;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapperUtil;
import pro.jiefzz.ejoker.z.system.wrapper.CountDownLatchWrapper;
import pro.jiefzz.ejoker.z.task.AsyncTaskResult;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

@MessageHandler
@EService
public class SyncHelper extends AbstractMessageHandler {

	private final static Logger logger = LoggerFactory.getLogger(SyncHelper.class);

	// for debug
	AtomicLong al1 = new AtomicLong(0);

	AtomicLong alt = new AtomicLong(0);

	public void show() {
		logger.error("DepositTransactionCompletedEvent hit: {}, last event hit: {} ms", al1.get(), alt.get());
	}
	
	public long getLastHitTimestamp() {
		return alt.get();
	}

	// for debug
	
	private Object waitHandle = CountDownLatchWrapper.newCountDownLatch();

	@Suspendable
	public void waitOne() {
		CountDownLatchWrapper.await(waitHandle);
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionCompletedEvent message) {
		al1.getAndIncrement();
		alt.set(System.currentTimeMillis());
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCompletedEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCanceledEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return SystemFutureWrapperUtil.completeFutureTask();
	}
}
