package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandReturnType;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.utils.EObjectId;

import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;

/**
 * mvn exec:java -Dexec.mainClass=pro.jiefzz.ejoker.demo.simple.transfer.TransferApp
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"
 * @author kimffy
 *
 */
public class TransferApp {

	private final static Logger logger = LoggerFactory.getLogger(TransferApp.class);
	
	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initDomainEventConsumer();
		eJokerFrameworkInitializer.initDomainEventPublisher();
		eJokerFrameworkInitializer.initCommandConsumer();
		
		CommandService commandService = eJokerFrameworkInitializer.initCommandService();

//		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
//		SystemAsyncHelper systemAsyncHelper = eJokerContext.get(SystemAsyncHelper.class);
//		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
//		scheduleService.startTask("afrqgqhersxx", () -> {
//			systemAsyncHelper.d1();
//			DevUtils.moniter();
//		}, 200l, 200l);
		
		
		TimeUnit.SECONDS.sleep(1l);
		System.out.println("");
		System.out.println("====================== TransferAPP ======================");
		System.out.println("");
		

		String[] owners = new String[] {
				"龙轩",
				"金飞",
				"包包",
				"JiefzzLon",
				"Kimffy",
				"SamelodyLau",
				"Coco",
				"雯雯",
				"寿司司",
				"晴阳",
				"龙轩_1",
				"金飞_2",
				"包包_3",
				"JiefzzLon_4",
				"Kimffy_5",
				"SamelodyLau_6",
				"Coco_7",
				"雯雯_8",
				"寿司司_9",
				"晴阳_0"
		};

		String[] ids = new String[] {
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId(),
				EObjectId.generateHexStringId()
		};
		
		for(int i=0; i<ids.length; i++) {
			CreateAccountCommand createAccountCommand = new CreateAccountCommand(ids[i], owners[i]);
			commandService.executeAsync(createAccountCommand, CommandReturnType.EventHandled).get();
		}
		
		System.out.println("Waiting... ");
		TimeUnit.SECONDS.sleep(20l);
		System.out.println("Start batch deposit... ");
		
		int loop = 10;
		long t = System.currentTimeMillis();
		for(int j=0; j<loop; j++) {
			if(j%2 == 0)
				TimeUnit.MICROSECONDS.sleep(500l);
			for(int i=0; i<ids.length; i++) {
				commandService.sendAsync(new StartDepositTransactionCommand(EObjectId.generateHexStringId(), ids[i], i%2==0?110:240));
//				commandService.executeAsync(new StartDepositTransactionCommand(EObjectId.generateHexStringId(), ids[i], i%2==0?110:240)).get();
			}
		}
		logger.error("time use: {} ms", System.currentTimeMillis() - t);

		LockSupport.park();
		
//		TimeUnit.SECONDS.sleep(20l);
//		DevUtils.ttt();
//		DevUtils.moniter();
//		ioHelper.d1();
//		systemAsyncHelper.d1();
//		LockSupport.park();
		
		
		eJokerFrameworkInitializer.discard();
		
	}
}
