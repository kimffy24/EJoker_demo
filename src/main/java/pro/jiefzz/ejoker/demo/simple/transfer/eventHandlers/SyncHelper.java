package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.system.extension.AsyncWrapperException;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.wrapper.CountDownLatchWrapper;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;

@MessageHandler
@EService
public class SyncHelper extends AbstractMessageHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(SyncHelper.class);
	
	AtomicLong al1 = new AtomicLong(0);
	
	public void show() {
		logger.error("DepositTransactionCompletedEvent hit: {}", al1.get());
		
	}

	private Object waitHandle = CountDownLatchWrapper.newCountDownLatch();
	
	
	public void waitOne() {
		try {
			CountDownLatchWrapper.await(waitHandle);
		} catch (AsyncWrapperException e) {
			e.printStackTrace();
		}
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionCompletedEvent message) {
		al1.getAndIncrement();
		CountDownLatchWrapper.countDown(waitHandle);
		waitHandle = CountDownLatchWrapper.newCountDownLatch();

		return SystemFutureWrapperUtil.createCompleteFutureTask();
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
