package pro.jiefzz.ejoker.test.functional;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;

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
