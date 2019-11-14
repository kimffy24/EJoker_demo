package pro.jiefzz.eden.multiMessage.bus;

import java.util.concurrent.Future;

import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class TestDEHandler extends AbstractMessageHandler {

	public Future<AsyncTaskResult<Void>> handleAsync(DE2 z) {
		System.err.println("DE2");
		return EJokerFutureTaskUtil.completeTask();
	}

	public Future<AsyncTaskResult<Void>> handleAsync(DE3 e, DE1 ex, DE2 ey) {
		System.err.println("DE3, DE1, DE2");
		return EJokerFutureTaskUtil.completeTask();
	}

	public Future<AsyncTaskResult<Void>> handleAsync(DE1 e, DE4 ex, DE2 ey) {
		System.err.println("DE1, DE4, DE2");
		return EJokerFutureTaskUtil.completeTask();
	}

	public Future<AsyncTaskResult<Void>> handleAsync(DE5 e, DE3 ex, DE1 ey, DE4 ez) {
		System.err.println("DE5, DE3, DE1, DE4");
		return EJokerFutureTaskUtil.completeTask();
	}

	public Future<AsyncTaskResult<Void>> handleAsync(DE8 e, DE4 ex, DE2 ey, DE6 ez) {
		System.err.println("DE8, DE4, DE2, DE6 from TestDEHandler");
		return EJokerFutureTaskUtil.completeTask();
	}

	public Future<AsyncTaskResult<Void>> handleAsync(DE8 e, DE7 ex, DE5 ey, DE4 ez) {
		System.err.println("DE8, DE7, DE5, DE4");
		return EJokerFutureTaskUtil.completeTask();
	}
}
