package pro.jiefzz.eden.multiMessage.bus;

import java.util.concurrent.Future;

import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.ESType;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.z.system.task.AsyncTaskResult;

@EService(type = ESType.MESSAGE_HANDLER)
public class TestDEHandler2 extends AbstractMessageHandler {

	public Future<AsyncTaskResult<Void>> handleAsync(DE8 e, DE4 ex, DE2 ey, DE6 ez) {
		System.err.println("DE8, DE4, DE2, DE6 from TestDEHandler2");
		return EJokerFutureTaskUtil.completeTask();
	}

}
