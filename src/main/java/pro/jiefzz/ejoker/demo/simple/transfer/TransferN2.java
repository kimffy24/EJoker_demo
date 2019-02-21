package pro.jiefzz.ejoker.demo.simple.transfer;

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
import com.jiefzz.ejoker.queue.command.CommandResultProcessor;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.utils.EObjectId;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;

/**
 * mvn exec:java -Dexec.mainClass=pro.jiefzz.ejoker.demo.simple.transfer.TransferApp
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"
 * @author kimffy
 *
 */
public class TransferN2 {

	private final static Logger logger = LoggerFactory.getLogger(TransferN2.class);
	
	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		CommandService commandService = eJokerFrameworkInitializer.initCommandService();
		
		IJSONConverter jsonConverter = eJokerFrameworkInitializer.getEJokerContext().get(IJSONConverter.class);
		ITopicProvider<ICommand> topicProvider = eJokerFrameworkInitializer.getEJokerContext().get(ITopicProvider.class, ICommand.class);
		ICommandRoutingKeyProvider commandRoutingKeyProvider = eJokerFrameworkInitializer.getEJokerContext().get(ICommandRoutingKeyProvider.class);
		SystemAsyncHelper systemAsyncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SystemAsyncHelper.class);
		IScheduleService scheduleService = eJokerFrameworkInitializer.getEJokerContext().get(IScheduleService.class);
		
		TimeUnit.SECONDS.sleep(1l);
		System.out.println("");
		System.out.println("====================== TransferAPP ======================");
		System.out.println("");
		
		int accountLoop = 100000;

		String[] ids = new String[accountLoop];

		for(int i=0; i<ids.length; i++) {
			ids[i] = ObjectId.get().toHexString();
			System.err.println(ids[i]);
		}
		
		final SystemFutureWrapper[] sfws = new SystemFutureWrapper[accountLoop];
		final AtomicInteger cursor = new AtomicInteger(0);
		for(int i=0; i<ids.length; i++) {
			systemAsyncHelper.submit(() -> {
				int index = cursor.getAndIncrement();
				CreateAccountCommand createAccountCommand = new CreateAccountCommand(ids[index], "owner_" + index);
				sfws[index] = commandService.executeAsync(createAccountCommand, CommandReturnType.EventHandled);
//				sfws[index] = commandService.sendAsync(createAccountCommand);
			});
			System.err.println("send No." + i);
		}
		System.err.println("send ok.");

		while(true) {
			boolean ok = true;
			for(int i=0; i<sfws.length; i++) {
				if(null != sfws[i] && sfws[i].isDone()) {
					ok &= true;
				} else {
					ok &= false;
					System.err.println("task " + i + " is still on running ... ");
					System.err.println(sfws[i]);
				}
			}
			if(ok)
				break;
			System.err.println();
			TimeUnit.MILLISECONDS.sleep(500l);
			
		}
		
//		System.exit(0);
		
		TimeUnit.MILLISECONDS.sleep(EJokerBootstrap.BatchDelay);
		System.out.println("Start batch deposit... ");
		
		int loop = 1;
		int amount = loop*ids.length;
		SystemFutureWrapper[] results = new SystemFutureWrapper[amount];
		long t = System.currentTimeMillis();
		for(int j=0; j<loop; j++) {
			for(int i=0; i<ids.length; i++) {
				StartDepositTransactionCommand startDepositTransactionCommand = new StartDepositTransactionCommand(EObjectId.generateHexStringId(), ids[i], i%2==0?110:240);
				results[(j*ids.length)+i] = commandService.sendAsync(startDepositTransactionCommand);
			}
		}
		while(true) {
			boolean ok = true;
			for(SystemFutureWrapper sfw:results) {
				try {
					sfw.get();
				} catch (Exception e) {
					e.printStackTrace();
					ok &= false;
				}
			}
			if(ok)
				break;
		}
		String msg = String.format("batch start at: %d, time use: %d ms", t, System.currentTimeMillis() - t);
		logger.error(msg);
		System.err.println(msg);
		
		
//		TimeUnit.SECONDS.sleep(20l);
//		DevUtils.ttt();
//		DevUtils.moniter();
//		ioHelper.d1();
//		systemAsyncHelper.d1();

		LockSupport.park();
		eJokerFrameworkInitializer.discard();
		
	}
}
