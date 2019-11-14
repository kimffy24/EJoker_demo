package pro.jiefzz.eden.multiMessage;

import java.util.Arrays;

import pro.jiefzz.eden.multiMessage.bus.DE2;
import pro.jiefzz.eden.multiMessage.bus.DE4;
import pro.jiefzz.eden.multiMessage.bus.DE6;
import pro.jiefzz.eden.multiMessage.bus.DE8;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.messaging.IMessageDispatcher;

public class TestMultiMessaeg {

	public static void main(String[] args) {
		
		EJokerBootstrap eJokerBootstrap = new EJokerBootstrap();
		
		
		IEJokerSimpleContext eJokerContext = eJokerBootstrap.getEJokerContext();
		
		IMessageDispatcher md = eJokerContext.get(IMessageDispatcher.class);
		
		md.dispatchMessagesAsync(Arrays.asList(new DE2(), new DE6(), new DE8(), new DE4()));

//		md.dispatchMessagesAsync(Arrays.asList(new DE4(), new DE7(), new DE5(), new DE8()));
	}

}
