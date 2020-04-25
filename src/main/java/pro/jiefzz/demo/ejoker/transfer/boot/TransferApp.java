package pro.jiefzz.demo.ejoker.transfer.boot;

import static pro.jk.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.TimeUnit;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.StartTransferTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.debug.DevUtils;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.demo.ejoker.transfer.eventHandlers.SyncHelper;
import pro.jk.ejoker.bootstrap.EJokerBootstrap;
import pro.jk.ejoker.commanding.CommandReturnType;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.service.IScheduleService;
import pro.jk.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jk.ejoker.queue.command.CommandService;

public class TransferApp {
	
	private final static Logger logger = LoggerFactory.getLogger(TransferApp.class);

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		scheduleService.startTask("doProbe", () -> DevUtils.moniterQ(eJokerContext), 5000, 5000);
		
		CommandService commandService = eJokerContext.get(CommandService.class);
		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		
		String account1 = ObjectId.get().toHexString();
		String account2 = ObjectId.get().toHexString();
		String account3 = "INVALID-" + ObjectId.get().toHexString();
		
		//创建两个银行账户
		
        commandService.executeAsync(new CreateAccountCommand(account1, "雪华"), CommandReturnType.EventHandled).get();
        commandService.executeAsync(new CreateAccountCommand(account2, "凯锋"), CommandReturnType.EventHandled).get();
        System.out.println();
        
        String depositId1 = ObjectId.get().toHexString();
		String depositId2 = ObjectId.get().toHexString();
        //每个账户都存入1000元
        await(commandService.sendAsync(new StartDepositTransactionCommand(depositId1, account1, 1000)));
        syncHelper.waitOne();
        await(commandService.sendAsync(new StartDepositTransactionCommand(depositId2, account2, 1000)));
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

        DiscardWrapper.sleep(TimeUnit.SECONDS, 1l);

        logger.info("All OK.");
        TimeUnit.SECONDS.sleep(15l);
        eJokerFrameworkInitializer.discard();

	}
}
