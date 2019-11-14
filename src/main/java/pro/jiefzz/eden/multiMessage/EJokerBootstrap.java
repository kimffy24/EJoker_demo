package pro.jiefzz.eden.multiMessage;

import pro.jiefzz.ejoker.EJoker;
import pro.jiefzz.ejoker.EJoker.EJokerSingletonFactory;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.common.context.dev2.IEjokerContextDev2;

public class EJokerBootstrap {

	private final static String EJokerDefaultImplPackage = "pro.jiefzz.ejoker_support.defaultMemoryImpl";
	
	private final static String BusinessPackage = "pro.jiefzz.eden.multiMessage";
	
	public final static String NameServAddr = "127.0.0.1:9876";
	
	private final IEJokerSimpleContext eJokerContext;

	public EJokerBootstrap() {

		System.out.println("");
		System.out.println("====================== EJokerFramework ======================");
		System.out.println("");
		
		EJoker eJokerInstance = new EJokerSingletonFactory(EJoker.class).getInstance();
		eJokerContext = eJokerInstance.getEJokerContext();
		
		{
			IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;
			eJokerFullContext.scanPackage(EJokerDefaultImplPackage);
			eJokerFullContext.scanPackage(BusinessPackage);
			eJokerFullContext.scanPackage("pro.jiefzz.eden.aa");
			eJokerFullContext.refresh();
		}
	}

	public IEJokerSimpleContext getEJokerContext() {
		return eJokerContext;
	}

	public void discard() {
		IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;
		eJokerFullContext.discard();
	}

}
