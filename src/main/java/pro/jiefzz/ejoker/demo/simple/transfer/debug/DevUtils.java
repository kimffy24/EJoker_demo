package pro.jiefzz.ejoker.demo.simple.transfer.debug;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;

import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.ConsoleLogger;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;

public final class DevUtils {
	
	public static long moniterQ() {

		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();

		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		ConsoleLogger testConsoleHelper = eJokerContext.get(ConsoleLogger.class);
		
		testConsoleHelper.show();
		syncHelper.show();
		
		return System.currentTimeMillis();
	}
}
