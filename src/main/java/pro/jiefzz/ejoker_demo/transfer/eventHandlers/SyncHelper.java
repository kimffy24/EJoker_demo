package pro.jiefzz.ejoker_demo.transfer.eventHandlers;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.ESType;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.z.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.z.system.wrapper.CountDownLatchWrapper;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

@EService(type = ESType.MESSAGE_HANDLER)
public class SyncHelper extends AbstractMessageHandler {

	private Object waitHandle = CountDownLatchWrapper.newCountDownLatch();

	@Suspendable
	public void waitOne() {
		try {
			CountDownLatchWrapper.await(waitHandle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(DepositTransactionCompletedEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureTaskUtil.completeTask();
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransferTransactionCompletedEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureTaskUtil.completeTask();
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransferTransactionCanceledEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureTaskUtil.completeTask();
	}
}
