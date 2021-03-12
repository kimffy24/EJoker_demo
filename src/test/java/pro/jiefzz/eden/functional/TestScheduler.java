package pro.jiefzz.eden.functional;

import pro.jk.ejoker.EJoker;
import pro.jk.ejoker.EJoker.EJokerSingletonFactory;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.context.dev2.IEjokerContextDev2;
import pro.jk.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jk.ejoker.common.service.IScheduleService;

public class TestScheduler {

	public static void main(String[] args) {

		EJoker eJokerInstance = new EJokerSingletonFactory(EJoker.class).getInstance();
		IEJokerSimpleContext eJokerContext = eJokerInstance.getEJokerContext();
		
		{
			IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;

			((EjokerContextDev2Impl )eJokerFullContext).getEJokerRootDefinationStore().scanPackage("pro.jiefzz.ejoker.test.context");
			
			eJokerFullContext.refresh();
		}
		
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		
		scheduleService.startTask("test", () -> {
			System.err.println(System.currentTimeMillis());
		}, 200, 200);
		
	}
}
