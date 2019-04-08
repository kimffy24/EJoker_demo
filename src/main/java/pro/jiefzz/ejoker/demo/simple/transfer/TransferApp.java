package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandReturnType;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.TopicReference;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.StartTransferTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;

public class TransferApp {
	
	private final static Logger logger = LoggerFactory.getLogger(TransferApp.class);
	
	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		eJokerFrameworkInitializer.initAll();
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		
//		{
//			IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
//			CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
//			final DefaultMQConsumer deeplyConsumer = (DefaultMQConsumer )commandConsumer.getDeeplyConsumer();
//			scheduleService.startTask("testRebalance", () -> {
//				try {
//					Set<MessageQueue> fetchMessageQueuesInBalance = deeplyConsumer.fetchMessageQueuesInBalance(TopicReference.CommandTopic);
//					System.err.println("fetchMessageQueuesInBalance.hash(): " + fetchMessageQueuesInBalance.hashCode());
//				} catch (MQClientException e) {
//					e.printStackTrace();
//				}
//				
//			}, 5000l, 1000l);
//			
//		}
		
		
		CommandService commandService = eJokerContext.get(CommandService.class);
		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		
		String account1 = ObjectId.get().toHexString();
		String account2 = ObjectId.get().toHexString();
		String account3 = "INVALID-" + ObjectId.get().toHexString();
		
		//创建两个银行账户
        commandService.executeAsync(new CreateAccountCommand(account1, "雪华"), CommandReturnType.EventHandled).get();
        commandService.executeAsync(new CreateAccountCommand(account2, "凯锋"), CommandReturnType.EventHandled).get();
        
        System.out.println();
        
        //每个账户都存入1000元
        commandService.sendAsync(new StartDepositTransactionCommand(ObjectId.get().toHexString(), account1, 1000)).get();
        syncHelper.waitOne();
        commandService.sendAsync(new StartDepositTransactionCommand(ObjectId.get().toHexString(), account2, 1000)).get();
        syncHelper.waitOne();
        
        System.out.println();

        //账户1向账户3转账300元，交易会失败，因为账户3不存在
        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account1, account3, 300D))).get();
        syncHelper.waitOne();
        
        System.out.println();

        //账户1向账户2转账1200元，交易会失败，因为余额不足
        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account1, account2, 1200D))).get();
        syncHelper.waitOne();
        
        System.out.println();

        //账户2向账户1转账500元，交易成功
        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account2, account1, 500D))).get();
        syncHelper.waitOne();

        SleepWrapper.sleep(TimeUnit.SECONDS, 1l);

        logger.info("All OK.");

	}
}
