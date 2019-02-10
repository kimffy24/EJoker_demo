package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.ICommand;
import com.jiefzz.ejoker.commanding.ICommandRoutingKeyProvider;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.queue.command.CommandResultProcessor;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.utils.EObjectId;
import com.jiefzz.ejoker.z.common.io.AsyncTaskResult;
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
		
		CommandResultProcessor commandResultProcessor = eJokerFrameworkInitializer.getEJokerContext().get(CommandResultProcessor.class);
		
		IJSONConverter jsonConverter = eJokerFrameworkInitializer.getEJokerContext().get(IJSONConverter.class);
		ITopicProvider<ICommand> topicProvider = eJokerFrameworkInitializer.getEJokerContext().get(ITopicProvider.class, ICommand.class);
		ICommandRoutingKeyProvider commandRoutingKeyProvider = eJokerFrameworkInitializer.getEJokerContext().get(ICommandRoutingKeyProvider.class);
		SystemAsyncHelper systemAsyncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SystemAsyncHelper.class);
		
		TimeUnit.SECONDS.sleep(1l);
		System.out.println("");
		System.out.println("====================== TransferAPP ======================");
		System.out.println("");
		
		int accountLoop = 100000;

		String[] ids = new String[accountLoop];

		for(int i=0; i<ids.length; i++) {
			ids[i] = ObjectId.get().toHexString();
		}
		
		SystemFutureWrapper[] sfws = new SystemFutureWrapper[accountLoop];
		
		for(int i=0; i<ids.length; i++) {
			final int j = i;
			CreateAccountCommand createAccountCommand = new CreateAccountCommand(ids[i], "owner_" + i);
			systemAsyncHelper.submit(() -> {
//				SystemFutureWrapper<AsyncTaskResult<CommandResult>> x = commandService.executeAsync(createAccountCommand, CommandReturnType.EventHandled);
				SystemFutureWrapper<AsyncTaskResult<Void>> x = commandService.sendAsync(createAccountCommand);
				sfws[j] = x;
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
		System.exit(0);
		
		TimeUnit.MILLISECONDS.sleep(EJokerBootstrap.BatchDelay);
		System.out.println("Start batch deposit... ");
		
		LockSupport.park();
		
		int loop = 10;
		int amount = loop*ids.length;
		SystemFutureWrapper[] results = new SystemFutureWrapper[amount];
		long t = System.currentTimeMillis();
		for(int j=0; j<loop; j++) {
//			if(j%2 == 0)
//				TimeUnit.MICROSECONDS.sleep(500l);
			for(int i=0; i<ids.length; i++) {
				StartDepositTransactionCommand startDepositTransactionCommand = new StartDepositTransactionCommand(EObjectId.generateHexStringId(), ids[i], i%2==0?110:240);
				results[--amount] = commandService.sendAsync(startDepositTransactionCommand);
//				commandService.executeAsync(startDepositTransactionCommand).get();
//				testSend(commandRoutingKeyProvider, jsonConverter, topicProvider, commandService.getProducer(), startDepositTransactionCommand, null);
			}
		}
		for(SystemFutureWrapper sfw:results) {
//			sfw.get();
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
