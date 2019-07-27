package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.wrapper.CountDownLatchWrapper;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

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
