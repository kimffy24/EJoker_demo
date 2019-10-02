package pro.jiefzz.eden.functional;

import pro.jiefzz.ejoker.EJoker;
import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.z.context.dev2.IEjokerContextDev2;
import pro.jiefzz.ejoker.z.service.IScheduleService;

public class TestScheduler {

	public static void main(String[] args) {

		EJoker eJokerInstance = EJoker.getInstance();
		IEJokerSimpleContext eJokerContext = eJokerInstance.getEJokerContext();
		
		{
			IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;

			eJokerFullContext.scanPackage("pro.jiefzz.ejoker.test.context");
			
			eJokerFullContext.refresh();
		}
		
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		
		scheduleService.startTask("test", () -> {
			System.err.println(System.currentTimeMillis());
		}, 200, 200);
		
	}
}
