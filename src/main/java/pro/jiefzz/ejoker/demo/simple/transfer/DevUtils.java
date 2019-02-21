package pro.jiefzz.ejoker.demo.simple.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;

import pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount.BankAccountCommandHandler;
import pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount.DepositTransactionCommandHandlers;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.TestConsoleHelper;
import pro.jiefzz.ejoker.demo.simple.transfer.processManagers.DepositTransactionProcessManager;

public final class DevUtils {
	
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(DevUtils.class);

	public static long moniterC() {

		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();

		DepositTransactionCommandHandlers depositTransactionCommandHandlers = eJokerContext.get(DepositTransactionCommandHandlers.class);
		BankAccountCommandHandler bankAccountCommandHandler = eJokerContext.get(BankAccountCommandHandler.class);
		
		depositTransactionCommandHandlers.show1();
		bankAccountCommandHandler.show1();
		depositTransactionCommandHandlers.show2();
		bankAccountCommandHandler.show2();
		depositTransactionCommandHandlers.show3();
		
		return System.currentTimeMillis();
	}

	public static long moniterQ() {

		IEJokerSimpleContext eJokerContext = EJoker.getInstance().getEJokerContext();

		DepositTransactionProcessManager depositTransactionProcessManager = eJokerContext.get(DepositTransactionProcessManager.class);
		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		TestConsoleHelper testConsoleHelper = eJokerContext.get(TestConsoleHelper.class);
		
		testConsoleHelper.show();
		depositTransactionProcessManager.show1();
		syncHelper.show();
		depositTransactionProcessManager.show2();
		
		return System.currentTimeMillis();
	}
}
