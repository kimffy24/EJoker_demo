package pro.jiefzz.demo.ejoker.transfer.boot;

import static pro.jk.ejoker.common.system.extension.LangUtil.await;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.DepositTransaction;
import pro.jiefzz.demo.ejoker.transfer.eventHandlers.CountSyncHelper;
import pro.jiefzz.demo.ejoker.transfer.eventHandlers.SyncHelper;
import pro.jk.ejoker.commanding.CommandReturnType;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.system.task.context.SystemAsyncHelper;
import pro.jk.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jk.ejoker.eventing.DomainEventStream;
import pro.jk.ejoker.eventing.IEventStore;
import pro.jk.ejoker.eventing.impl.InMemoryEventStore;
import pro.jk.ejoker.infrastructure.ITypeNameProvider;
import pro.jk.ejoker.queue.command.CommandService;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;

public class TransferAppPerformanceTest {
	
	private final static Logger logger = LoggerFactory.getLogger(TransferAppPerformanceTest.class);

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		
		CommandService commandService = eJokerContext.get(CommandService.class);
		SyncHelper syncHelper = eJokerContext.get(SyncHelper.class);
		CountSyncHelper countSyncHelper = eJokerContext.get(CountSyncHelper.class);
		SystemAsyncHelper systemAsyncHelper = eJokerContext.get(SystemAsyncHelper.class);

        int accountCount = 10000;
		String[] accountList = new String[accountCount];
        double depositAmount = 1000000D;
        int transactionCount = 1000;
        double transferAmount = 1000D;
		
		//创建银行账户
        for(int i=0; i<accountCount; i++) {
        	final int j = i;
        	systemAsyncHelper.submit(() -> {
            	String accountId = ObjectId.get().toHexString();
            	await(commandService.executeAsync(new CreateAccountCommand(accountId, "SampleAccount" + j), CommandReturnType.EventHandled));
            	accountList[j] = accountId;
        	});
        }
        for(int i=0; i<accountCount; i++) {
        	if(null == accountList[i]) {
        		i--;
        		DiscardWrapper.sleep(TimeUnit.MILLISECONDS, 0l);
        	}
        }
        
        logger.error("账号创建 OK.");

        countSyncHelper.setExpectedCount(accountCount);
        //每个账户都存入初始额度
        for(String accountId:accountList) {
        	StartDepositTransactionCommand cmd = new StartDepositTransactionCommand(ObjectId.get().toHexString(), accountId, depositAmount);
        	systemAsyncHelper.submit(() -> await(commandService.sendAsync(cmd)));
        }
        countSyncHelper.waitOne();

        logger.error("每个账户都存入初始额度 OK.");
        
        IEventStore eventStore = eJokerContext.get(IEventStore.class);
        if(InMemoryEventStore.class.isAssignableFrom(eventStore.getClass())) {
        	ITypeNameProvider typeNameProvider = eJokerContext.get(ITypeNameProvider.class);
        	Field field = InMemoryEventStore.class.getDeclaredField("mStorage");
        	field.setAccessible(true);
        	Map<String, Map<String, DomainEventStream>> mStorage = (Map<String, Map<String, DomainEventStream>> )field.get(eventStore);
        	
        	Optional<Long> maxO = mStorage.entrySet().parallelStream().map(e -> {
        		Map<String, DomainEventStream> value = e.getValue();
        		DomainEventStream domainEventStream = value.get("3");
        		if(null == domainEventStream)
        			return 0l;
        		String typeName4DepositTransaction = typeNameProvider.getTypeName(DepositTransaction.class);
        		if(typeName4DepositTransaction.equals(domainEventStream.getAggregateRootTypeName())){
        			return domainEventStream.getTimestamp();
        		}
        		return 0l;
        	}).max((a, b) -> a-b < 0l ? -1 : 1);
        	Long maxTimestamp = maxO.get();

        	Optional<Long> minO = mStorage.entrySet().parallelStream().map(e -> {
        		Map<String, DomainEventStream> value = e.getValue();
        		DomainEventStream domainEventStream = value.get("1");
        		if(null == domainEventStream)
        			return Long.MAX_VALUE;
        		String typeName4DepositTransaction = typeNameProvider.getTypeName(DepositTransaction.class);
        		if(typeName4DepositTransaction.equals(domainEventStream.getAggregateRootTypeName())){
        			return domainEventStream.getTimestamp();
        		}
    			return Long.MAX_VALUE;
        	}).min((a, b) -> a-b < 0l ? -1 : 1);
        	Long minTimestamp = minO.get();
        	logger.error("use: {} ms, min {}, max {}", maxTimestamp - minTimestamp, minTimestamp, maxTimestamp);
        	double t = (maxTimestamp - minTimestamp)/1000;
        	logger.error("avg: {} /s", 5*accountCount*1000/(maxTimestamp - minTimestamp));
        	logger.error("avg: {} /ms", 5*accountCount/(maxTimestamp - minTimestamp));
        }
        
//
//        //账户1向账户3转账300元，交易会失败，因为账户3不存在
//        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account1, account3, 300D))).get();
//        syncHelper.waitOne();
//        
//        System.out.println();
//
//        //账户1向账户2转账1200元，交易会失败，因为余额不足
//        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account1, account2, 1200D))).get();
//        syncHelper.waitOne();
//        
//        System.out.println();
//
//        //账户2向账户1转账500元，交易成功
//        commandService.sendAsync(new StartTransferTransactionCommand(ObjectId.get().toHexString(), new TransferTransactionInfo(account2, account1, 500D))).get();
//        syncHelper.waitOne();

        DiscardWrapper.sleep(TimeUnit.SECONDS, 1l);

        logger.info("All OK.");

	}
}
