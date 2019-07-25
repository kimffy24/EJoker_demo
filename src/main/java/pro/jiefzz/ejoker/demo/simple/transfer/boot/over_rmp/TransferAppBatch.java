package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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

public class TransferAppBatch {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferAppBatch.class);

	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()), 400, 20);
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
		TimeUnit.MILLISECONDS.sleep(20000l);
		System.out.println("Start batch deposit... ");
		
		int amount = transferLoop*ids.length;
		CountDownLatch cdl = new CountDownLatch(amount);
		long t = System.currentTimeMillis();
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
		String msg = String.format("batch start at: %d, time use: %d ms", t, System.currentTimeMillis() - t);
		logger.error(msg);
		System.err.println(msg);
		
		
//		TimeUnit.SECONDS.sleep(20l);
//
//		eJokerFrameworkInitializer.discard();
		LockSupport.park();
	}
}
