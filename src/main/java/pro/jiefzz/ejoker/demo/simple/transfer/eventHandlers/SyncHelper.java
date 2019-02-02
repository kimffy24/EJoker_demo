package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.io.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.EJokerFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;

@MessageHandler
@EService
public class SyncHelper extends AbstractMessageHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(SyncHelper.class);
	
//	AtomicLong al1 = new AtomicLong(0);
//	
//	AtomicLong lastThousandHit = new AtomicLong(0);
//	
//	public void show() {
//		long Val1 = al1.get();
//		if(Val1%1000 == 0)
//			lastThousandHit.set(System.currentTimeMillis());
//		logger.error("DepositTransactionCompletedEvent hit: {}, lastThousandHit: {}. \n", Val1, lastThousandHit.get());
//		
//	}

	private CountDownLatch waitHandle = new CountDownLatch(1);
	
	public void waitOne() {
		try {
			waitHandle.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionCompletedEvent message) {
//		al1.getAndIncrement();
		waitHandle.countDown();
		waitHandle = new CountDownLatch(1);

		return EJokerFutureWrapperUtil.createCompleteFutureTask();
	}
//
//    public Task<AsyncTaskResult> HandleAsync(TransferTransactionCompletedEvent message)
//    {
//        _waitHandle.Set();
//        _waitHandle = new ManualResetEvent(false);
//        return Task.FromResult(AsyncTaskResult.Success);
//    }
//    public Task<AsyncTaskResult> HandleAsync(TransferTransactionCanceledEvent message)
//    {
//        _waitHandle.Set();
//        _waitHandle = new ManualResetEvent(false);
//        return Task.FromResult(AsyncTaskResult.Success);
//    }
}
