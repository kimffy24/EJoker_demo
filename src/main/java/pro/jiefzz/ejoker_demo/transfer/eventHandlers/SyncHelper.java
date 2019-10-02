package pro.jiefzz.ejoker_demo.transfer.eventHandlers;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.MessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jiefzz.ejoker.z.system.wrapper.CountDownLatchWrapper;
import pro.jiefzz.ejoker.z.task.AsyncTaskResult;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

@MessageHandler
@EService
public class SyncHelper extends AbstractMessageHandler {

	private Object waitHandle = CountDownLatchWrapper.newCountDownLatch();

	@Suspendable
	public void waitOne() {
		CountDownLatchWrapper.await(waitHandle);
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
