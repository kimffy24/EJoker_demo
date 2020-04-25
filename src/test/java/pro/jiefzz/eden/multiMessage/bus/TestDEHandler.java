package pro.jiefzz.eden.multiMessage.bus;

import java.util.concurrent.Future;

import pro.jk.ejoker.common.context.annotation.context.ESType;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class TestDEHandler extends AbstractMessageHandler {

	public Future<Void> handleAsync(DE2 z) {
		System.err.println("DE2");
		return EJokerFutureUtil.completeFuture();
	}

	public Future<Void> handleAsync(DE3 e, DE1 ex, DE2 ey) {
		System.err.println("DE3, DE1, DE2");
		return EJokerFutureUtil.completeFuture();
	}

	public Future<Void> handleAsync(DE1 e, DE4 ex, DE2 ey) {
		System.err.println("DE1, DE4, DE2");
		return EJokerFutureUtil.completeFuture();
	}

	public Future<Void> handleAsync(DE5 e, DE3 ex, DE1 ey, DE4 ez) {
		System.err.println("DE5, DE3, DE1, DE4");
		return EJokerFutureUtil.completeFuture();
	}

	public Future<Void> handleAsync(DE8 e, DE4 ex, DE2 ey, DE6 ez) {
		System.err.println("DE8, DE4, DE2, DE6 from TestDEHandler");
		return EJokerFutureUtil.completeFuture();
	}

	public Future<Void> handleAsync(DE8 e, DE7 ex, DE5 ey, DE4 ez) {
		System.err.println("DE8, DE7, DE5, DE4");
		return EJokerFutureUtil.completeFuture();
	}
}
