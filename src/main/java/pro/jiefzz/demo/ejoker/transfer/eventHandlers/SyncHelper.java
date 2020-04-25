package pro.jiefzz.demo.ejoker.transfer.eventHandlers;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;
import pro.jk.ejoker.common.context.annotation.context.ESType;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jk.ejoker.common.system.wrapper.CountDownLatchWrapper;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

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
	public Future<Void> handleAsync(DepositTransactionCompletedEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionCompletedEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionCanceledEvent message) {
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();
		return EJokerFutureUtil.completeFuture();
	}
}
