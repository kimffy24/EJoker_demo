package pro.jiefzz.demo.ejoker.transfer.boot;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.eventHandlers.ConsoleLogger;
import pro.jk.ejoker.commanding.CommandReturnType;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.system.extension.LangUtil;
import pro.jk.ejoker.common.system.task.context.SystemAsyncHelper;
import pro.jk.ejoker.common.system.task.io.IOHelper;
import pro.jk.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jk.ejoker.queue.command.CommandService;
import pro.jk.ejoker.utils.MObjectId;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;

public class TransferAppBatch {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferAppBatch.class);
	
	public final static int accountAmount;

	public final static int depositLoop;
	
	public final static int getAccountAmount() {
		return accountAmount;
	}
	
	public final static int getDepositLoop() {
		return depositLoop;
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		start(eJokerFrameworkInitializer, getAccountAmount(), getDepositLoop());
	}

	public static void start(EJokerBootstrap eJokerFrameworkInitializer, int accountAmount, int transferLoop) throws Exception {

		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		SystemAsyncHelper systemAsyncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SystemAsyncHelper.class);
		IOHelper ioHelper = eJokerFrameworkInitializer.getEJokerContext().get(IOHelper.class);
		ConsoleLogger consoleLogger = eJokerFrameworkInitializer.getEJokerContext().get(ConsoleLogger.class);
		
		TimeUnit.SECONDS.sleep(1l);
		System.out.println("");
		System.out.println("====================== TransferAPP ======================");
		System.out.println("");

		CommandService commandService = eJokerContext.get(CommandService.class);

		String[] ids = new String[accountAmount];

		for(int i=0; i<ids.length; i++) {
			ids[i] = MObjectId.get().toHexString();
		}
		
		final AtomicInteger cursor = new AtomicInteger(0);

		if(System.currentTimeMillis() < 0 ) {
		
		// ++++++++++++++ switch 1 : 通过 commandService.executeAsync
		
		for(int i=0; i<ids.length; i++) {
			int index = cursor.getAndIncrement();
			LangUtil.await(commandService.executeAsync(new CreateAccountCommand(ids[index], "owner_" + index), CommandReturnType.EventHandled));
		}
		logger.error("send ok.");

		} else {
		// ++++++++++++++ switch 2 : 通过 commandService.sendAsync
		
		final CountDownLatch cdlx = new CountDownLatch(accountAmount);
		for(int i=0; i<ids.length; i++) {
			
			systemAsyncHelper.submit(() -> {
				int index = cursor.getAndIncrement();
				ioHelper.tryAsyncAction2(
						"TestCreate_" + index,
						() -> commandService.sendAsync(new CreateAccountCommand(ids[index], "owner_" + index)),
						r -> cdlx.countDown(),
						() -> "",
						e -> logger.error("send faild 1!!!", e),
						true);
			});
			logger.error("send No." + i);
		}
		logger.error("send ok.");
		cdlx.await();
		
		// ++++++++++++++
		}
		
		while(consoleLogger.getAccountHit() < accountAmount)
			DiscardWrapper.sleepInterruptable(100l);
		logger.error("all account ok. ");
		
		DiscardWrapper.sleepInterruptable(TimeUnit.MILLISECONDS, 2000l);
		
		String latestAccountId = MObjectId.get().toHexString();
		consoleLogger.setLatestTransferId(latestAccountId);
		LangUtil.await(commandService.executeAsync(new CreateAccountCommand(latestAccountId, "owner_" + latestAccountId), CommandReturnType.EventHandled));
		
		DiscardWrapper.sleepInterruptable(TimeUnit.MILLISECONDS, 2000l);
		
		int amount = transferLoop*ids.length;
		AtomicBoolean exit = new AtomicBoolean(false);
		CountDownLatch cdl = new CountDownLatch(amount);
		Queue<StartDepositTransactionCommand> waitQ = new ConcurrentLinkedQueue<>();
		for(int tx = 0; tx<6; tx++)
			new Thread(() -> {
				while(true) {
					StartDepositTransactionCommand cmdx = waitQ.poll();
					if(null == cmdx) {
						DiscardWrapper.sleepInterruptable(3l);
						if(exit.get())
							break;
						continue;
					}
					ioHelper.tryAsyncAction2(
							"TestSend_" + cmdx.getAggregateRootId(),
							() -> commandService.sendAsync(cmdx),
							r -> cdl.countDown(),
							() -> "",
							e -> logger.error("send faild 2!!!", e),
							true);
				}
			}).start();
		
		long batchStartAt = System.currentTimeMillis();
		logger.error("deposit's cmd send task started.");
		for(int j=0; j<transferLoop; j++) {
			for(int i=0; i<ids.length; i++) {
				StartDepositTransactionCommand cmd = new StartDepositTransactionCommand(MObjectId.get().toHexString(), ids[i], i%2==0?110:240);
				waitQ.offer(cmd);
			}
		}
		
		cdl.await();
		

		// DiscardWrapper.sleepInterruptable(TimeUnit.MILLISECONDS, 2000l);
		StartDepositTransactionCommand cmd = new StartDepositTransactionCommand(MObjectId.get().toHexString(), latestAccountId, 567);
		waitQ.offer(cmd);
		consoleLogger.awaitEnd();
		
		exit.set(true);
		logger.error("deposit's cmd send task all completed.");
		logger.error("start at: {}, time use: {} ms", batchStartAt, System.currentTimeMillis() - batchStartAt);
        logger.error("Detect EAmount: {} .", accountAmount + 1);
        logger.error("Detect ELoop: {} .", depositLoop);
		
//		TimeUnit.SECONDS.sleep(20l);
//
//		eJokerFrameworkInitializer.discard();
		LockSupport.park();
	}


	static {
		String EAmount = null;
		String ELoop = null;
		boolean isWindows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
		
		String envKey1 = "EAmount";
		String envKey2 = "ELoop";
		if(isWindows) {
			// All environment propertie's name will represent by upper case.
//			envKey1 = envKey1.toUpperCase();
//			envKey2 = envKey1.toUpperCase();
		}
		
		Map<String, String> map = System.getenv();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ){
            String key = it.next();
            String value = map.get(key);
            if(envKey1.equals(key))
            	EAmount = value;
            if(envKey2.equals(key))
            	ELoop = value;
        }
        
        if(null == EAmount || "".equals(EAmount)) {
        	EAmount = "100";
        }
        if(null == ELoop || "".equals(ELoop)) {
        	ELoop = "10";
        }
        logger.info("Detect EAmount: {} .", EAmount);
        logger.info("Detect ELoop: {} .", ELoop);
        accountAmount = Integer.parseInt(EAmount);
        depositLoop = Integer.parseInt(ELoop);
	}
}
