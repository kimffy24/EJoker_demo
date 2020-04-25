package pro.jiefzz.eden.multiMessage.bus;

import java.util.concurrent.Future;

import pro.jk.ejoker.common.context.annotation.context.ESType;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class TestDEHandler2 extends AbstractMessageHandler {

	public Future<Void> handleAsync(DE8 e, DE4 ex, DE2 ey, DE6 ez) {
		System.err.println("DE8, DE4, DE2, DE6 from TestDEHandler2");
		return EJokerFutureUtil.completeFuture();
	}

}
