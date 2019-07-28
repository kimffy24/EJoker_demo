package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandReturnType;
import com.jiefzz.ejoker.commanding.ICommand;
import com.jiefzz.ejoker.commanding.ICommandRoutingKeyProvider;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.utils.MObjectId;
import com.jiefzz.ejoker.z.common.io.IOHelper;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.AbstractEJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.TransferPrepare;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.ConsoleLogger;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;

public class TransferAppBatch {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferAppBatch.class);
	
	public final static int accountAmount;

	public final static int depositLoop;

	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()), getAccountAmount(), getDepositLoop());
	}
	
	public final static int getAccountAmount() {
		return accountAmount;
	}
	
	public final static int getDepositLoop() {
		return depositLoop;
	}

	public static void start(AbstractEJokerBootstrap eJokerFrameworkInitializer, int accountAmount, int transferLoop) throws Exception {

		eJokerFrameworkInitializer.initAll();
		
		CommandService commandService = eJokerFrameworkInitializer.initCommandService();
		
		IJSONConverter jsonConverter = eJokerFrameworkInitializer.getEJokerContext().get(IJSONConverter.class);
		ITopicProvider<ICommand> topicProvider = eJokerFrameworkInitializer.getEJokerContext().get(ITopicProvider.class, ICommand.class);
		ICommandRoutingKeyProvider commandRoutingKeyProvider = eJokerFrameworkInitializer.getEJokerContext().get(ICommandRoutingKeyProvider.class);
		SystemAsyncHelper systemAsyncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SystemAsyncHelper.class);
		IScheduleService scheduleService = eJokerFrameworkInitializer.getEJokerContext().get(IScheduleService.class);
		IOHelper ioHelper = eJokerFrameworkInitializer.getEJokerContext().get(IOHelper.class);
		SyncHelper syncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SyncHelper.class);
		ConsoleLogger consoleLogger = eJokerFrameworkInitializer.getEJokerContext().get(ConsoleLogger.class);
		
		AtomicLong t = new AtomicLong(System.currentTimeMillis());
		
		scheduleService.startTask("ProformanceStatistics_1", () -> {
			try {
				BigDecimal totalProcess = new BigDecimal("10")
						.multiply(new BigDecimal("" + accountAmount))
						.multiply(new BigDecimal("" + transferLoop))
						.divide(
								new BigDecimal("" + (new Long(syncHelper.getLastHitTimestamp() - t.get()).intValue())).divide(new BigDecimal("1000")),
								RoundingMode.HALF_UP)
						;
				logger.error("message handled per second: {}", totalProcess.toPlainString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 5000l, 5000l);
		
		TimeUnit.SECONDS.sleep(1l);
		System.out.println("");
		System.out.println("====================== TransferAPP ======================");
		System.out.println("");
		
		

		String[] ids = new String[accountAmount];

		for(int i=0; i<ids.length; i++) {
			ids[i] = ObjectId.get().toHexString();
		}
		
		final AtomicInteger cursor = new AtomicInteger(0);
		final CountDownLatch cdlx = new CountDownLatch(accountAmount);
		for(int i=0; i<ids.length; i++) {
			systemAsyncHelper.submit(() -> {
				int index = cursor.getAndIncrement();
				ioHelper.tryAsyncAction2(
						"TestCreate_" + index,
//						() -> commandService.executeAsync(new CreateAccountCommand(ids[index], "owner_" + index), CommandReturnType.EventHandled),
						() -> commandService.sendAsync(new CreateAccountCommand(ids[index], "owner_" + index)),
						r -> cdlx.countDown(),
						() -> "",
						e -> e.printStackTrace(),
						true);
			});
			System.err.println("send No." + i);
		}
		System.err.println("send ok.");
		cdlx.await();
		System.err.println("all account ok. ");
//		System.exit(0);
		if(consoleLogger.getAccountHit() < accountAmount)
			TimeUnit.MILLISECONDS.sleep(1000l);
		

		t.set(System.currentTimeMillis());
		String msgp = String.format("Start batch deposit, batch start at: %d ... ", t.get());
		System.err.println(msgp);
		logger.error(msgp);
		
		int amount = transferLoop*ids.length;
		CountDownLatch cdl = new CountDownLatch(amount);
		for(int j=0; j<transferLoop; j++) {
			for(int i=0; i<ids.length; i++) {
				final int index = (j*ids.length)+i;
				StartDepositTransactionCommand startDepositTransactionCmd = new StartDepositTransactionCommand(MObjectId.get().toHexString(), ids[i], i%2==0?110:240);
				systemAsyncHelper.submit(() -> {
					if(index == amount - 1) {
						ioHelper.tryAsyncAction2(
								"TestSend_" + index,
								() -> commandService.executeAsync(startDepositTransactionCmd, CommandReturnType.EventHandled),
								r -> { cdl.countDown(); logger.error("actually complete at: {}", System.currentTimeMillis());},
								() -> "",
								e -> e.printStackTrace(),
								true);
					} else {
						ioHelper.tryAsyncAction2(
								"TestSend_" + index,
								() -> commandService.sendAsync(startDepositTransactionCmd),
								r -> cdl.countDown(),
								() -> "",
								e -> e.printStackTrace(),
								true);
					}
				});
			}
		}
		cdl.await();
		String msg = String.format("batch start at: %d, time use: %d ms", t.get(), System.currentTimeMillis() - t.get());
		System.err.println(msg);
		logger.error(msg);
		
		
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
			envKey1 = envKey1.toUpperCase();
			envKey2 = envKey1.toUpperCase();
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
