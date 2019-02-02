package pro.jiefzz.ejoker.test.nettyRpc;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;

public class EJokerBootstrap {

	private final static String EJokerDefaultImplPackage = "com.jiefzz.ejokerDefaultImpl";
	
	private final static String BusinessPackage = "pro.jiefzz.ejoker.test.nettyRpc";
	
	public final static String NameServAddr = "192.168.199.123:9876";
	
	private final IEJokerSimpleContext eJokerContext;

	public EJokerBootstrap() {

		System.out.println("");
		System.out.println("====================== EJokerFramework ======================");
		System.out.println("");
		
		EJoker eJokerInstance = EJoker.getInstance();
		eJokerContext = eJokerInstance.getEJokerContext();
		
		{
			IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;
			eJokerFullContext.scanPackage(EJokerDefaultImplPackage);
			eJokerFullContext.scanPackage(BusinessPackage);
			eJokerFullContext.scanPackage("pro.jiefzz.ejoker.demo.simple.transfer.topicProviders");
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
