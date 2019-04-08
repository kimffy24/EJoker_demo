package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;

/**
 * 这是一个入口端的demo<br />
 * 主要用来创建账号和发送存款命令
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms2g -Xmx4g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferFrontend"
 * <br />* 远程调试添加到exec.args中 


 * 
 * @author kimffy
 *
 */
public class TransferFrontend {
	
	public final static int accountAmount = 200000;

	public final static int transferLoop = 6;
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferFrontend.class);

	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

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
			if(i > 0 && 0 == i % 10000)
				SleepWrapper.sleep(TimeUnit.SECONDS, 1l);;
			ids[i] = ObjectId.get().toHexString();
		}
		
		final AtomicInteger cursor = new AtomicInteger(0);
		final CountDownLatch cdlx = new CountDownLatch(accountAmount);
		for(int i=0; i<ids.length; i++) {
			systemAsyncHelper.submit(() -> {
				int index = cursor.getAndIncrement();
				ioHelper.tryAsyncAction2(
						"TestCreate_" + index,
						() -> commandService.executeAsync(new CreateAccountCommand(ids[index], "owner_" + index), CommandReturnType.EventHandled),
						r -> cdlx.countDown(),
						() -> "",
						e -> e.printStackTrace(),
						true);
				
			});
			System.err.println("send No." + i);
		}
		System.err.println("send ok.");
		
//		System.exit(0);
		
		TimeUnit.MILLISECONDS.sleep(EJokerBootstrap.BatchDelay);
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
//		DevUtils.ttt();
//		DevUtils.moniter();
//		ioHelper.d1();
//		systemAsyncHelper.d1();

		eJokerFrameworkInitializer.discard();
	}
}
