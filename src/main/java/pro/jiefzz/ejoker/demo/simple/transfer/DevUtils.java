package pro.jiefzz.ejoker.demo.simple.transfer;

import static com.jiefzz.ejoker.z.common.utils.ForEachUtil.processForEach;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.commanding.ProcessingCommandMailbox;
import com.jiefzz.ejoker.commanding.impl.DefaultCommandProcessor;
import com.jiefzz.ejoker.domain.impl.DefaultMemoryCache;
import com.jiefzz.ejoker.eventing.DomainEventStreamMessage;
import com.jiefzz.ejoker.infrastructure.ProcessingMessageMailbox;
import com.jiefzz.ejoker.infrastructure.impl.AbstractDefaultMessageProcessor;
import com.jiefzz.ejoker.infrastructure.varieties.domainEventStreamMessage.ProcessingDomainEventStreamMessage;
import com.jiefzz.ejoker.infrastructure.varieties.domainEventStreamMessage.impl.DefaultDomainEventStreamMessageProcessor;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.impl.EjokerContextDev2Impl;
import com.jiefzz.ejoker.z.common.utils.ForEachUtil;

import pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount.BankAccountCommandHandler;
import pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount.DepositTransactionCommandHandlers;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;
import pro.jiefzz.ejoker.demo.simple.transfer.processManagers.DepositTransactionProcessManager;

public final class DevUtils {
	
	private final static Logger logger = LoggerFactory.getLogger(DevUtils.class);

	public static int ttt() throws Exception {
		
		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();
		
		Field field = EjokerContextDev2Impl.class.getDeclaredField("instanceMap");
		field.setAccessible(true);
		
		Object object = field.get(eJokerContext);
		
		Map<String, Object> im = (Map<String, Object> )object;
		
		Object object2 = im.get(DefaultMemoryCache.class.getName());
		
		DefaultMemoryCache dmc = (DefaultMemoryCache )object2;
		
		Field field2 = DefaultMemoryCache.class.getDeclaredField("aggregateRootInfoDict");
		field2.setAccessible(true);
		
		Object object3 = field2.get(dmc);
		
		Map<String, com.jiefzz.ejoker.domain.AggregateCacheInfo> arid = (Map<String, com.jiefzz.ejoker.domain.AggregateCacheInfo> )object3;
		
		processForEach(arid, (p1, p2) -> logger.debug(p2.aggregateRoot.getClass().getName() + " " + p2.aggregateRoot.getVersion()));
	
		return 1;
	}


	public static Object ttt(String instanceKey) throws Exception {
		
		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();
		
		Field field = EjokerContextDev2Impl.class.getDeclaredField("instanceMap");
		field.setAccessible(true);
		
		Object object = field.get(eJokerContext);
		
		Map<String, Object> im = (Map<String, Object> )object;
		
		return im.get(instanceKey);
		
	}
	
	public static Object ttt1() throws Exception {
		DefaultDomainEventStreamMessageProcessor x = ((DefaultDomainEventStreamMessageProcessor )ttt("com.jiefzz.ejoker.infrastructure.varieties.domainEventStreamMessage.impl.DefaultDomainEventStreamMessageProcessor"));
		Field field = AbstractDefaultMessageProcessor.class.getDeclaredField("mailboxDict");
		field.setAccessible(true);
		
		Object object = field.get(x);

		Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> y = (Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> )object;
		
		ForEachUtil.processForEach(y, (k, v) -> {
//			System.err.println(v.onRunning());
		});
		return null;
	}
	
	public static Object ttt2() throws Exception {
		DefaultDomainEventStreamMessageProcessor x = ((DefaultDomainEventStreamMessageProcessor )ttt(DefaultCommandProcessor.class.getName()));
		Field field = AbstractDefaultMessageProcessor.class.getDeclaredField("mailboxDict");
		field.setAccessible(true);
		
		Object object = field.get(x);

		Map<String, ProcessingCommandMailbox> y = (Map<String, ProcessingCommandMailbox> )object;
		
		ForEachUtil.processForEach(y, (k, v) -> {
//			System.err.println(v.onRunning());
		});
		return null;
	}
	
//	public static long moniter() {
//		
//
//		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();
//
//		DepositTransactionCommandHandlers depositTransactionCommandHandlers = eJokerContext.get(DepositTransactionCommandHandlers.class);
//		BankAccountCommandHandler bankAccountCommandHandler = eJokerContext.get(BankAccountCommandHandler.class);
//		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
//		
//		depositTransactionCommandHandlers.show1();
//		bankAccountCommandHandler.show1();
//		depositTransactionCommandHandlers.show2();
//		bankAccountCommandHandler.show2();
//		depositTransactionCommandHandlers.show3();
//		
//		DepositTransactionProcessManager depositTransactionProcessManager = eJokerContext.get(DepositTransactionProcessManager.class);
//		depositTransactionProcessManager.show();
//		syncHelper.show();
//		
//		logger.debug("OK!");
//		return System.currentTimeMillis();
//	}
}
