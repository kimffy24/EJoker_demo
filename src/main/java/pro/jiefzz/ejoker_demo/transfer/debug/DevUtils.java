package pro.jiefzz.ejoker_demo.transfer.debug;

import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker_demo.transfer.eventHandlers.ConsoleLogger;
import pro.jiefzz.ejoker_demo.transfer.eventHandlers.SyncHelper;

public final class DevUtils {
	
	public static long moniterQ(IEJokerSimpleContext eJokerContext) {

		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		ConsoleLogger testConsoleHelper = eJokerContext.get(ConsoleLogger.class);
		
		testConsoleHelper.show();
		syncHelper.show();
		
		return System.currentTimeMillis();
	}
}
