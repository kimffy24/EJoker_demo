package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandReturnType;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.utils.EObjectId;

import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;

public class TransferFrontend {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferFrontend.class);

	public static void main(String[] args) throws Exception {

		EJokerBootstrap eJokerFrameworkInitializer = new EJokerBootstrap();

		
		CommandService commandService = eJokerFrameworkInitializer.initCommandService();
		
		SyncHelper syncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SyncHelper.class);

		System.out.println("");
		System.out.println("====================== TransferN1 ======================");
		System.out.println("");
		
		
		CountDownLatch cdl = new CountDownLatch(1);
		
		String account1 = EObjectId.generateHexStringId();
		String account2 = EObjectId.generateHexStringId();
		
//		String account1 = "60f53c7bdfd3589776ff30484";
//		String account2 = "42d25a7bdfddeeda728011d32";
		
		CreateAccountCommand createAccountCommand1 = new CreateAccountCommand(account1, "龙轩1");
		commandService.executeAsync(createAccountCommand1, CommandReturnType.EventHandled).get();
		
		CreateAccountCommand createAccountCommand2 = new CreateAccountCommand(account2, "金飞2");
		commandService.executeAsync(createAccountCommand2, CommandReturnType.EventHandled).get();


		String xt1 = EObjectId.generateHexStringId();
		String xt2 = EObjectId.generateHexStringId();
		
		commandService.sendAsync(new StartDepositTransactionCommand(xt1, account1, 1000));
		
		commandService.sendAsync(new StartDepositTransactionCommand(xt2, account2, 2500));
		
//		System.out.println("Waiting... ");
//		TimeUnit.SECONDS.sleep(3l);
//		System.out.println("Start batch deposit... ");
		
		long t = System.currentTimeMillis();
		for(int i=0; i<500; i++) {
			String t1 = EObjectId.generateHexStringId();
			String t2 = EObjectId.generateHexStringId();
			
			commandService.sendAsync(new StartDepositTransactionCommand(t1, account1, 1100));
			commandService.sendAsync(new StartDepositTransactionCommand(t2, account2, 2400));
			
			System.err.println(t1);
			System.err.println(t2);
		}
		logger.debug("time use: {} ms", System.currentTimeMillis() - t);
		
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(10l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cdl.countDown();
		}).start();
		
		cdl.await();
		LockSupport.park();
		eJokerFrameworkInitializer.discard();
	}

}
