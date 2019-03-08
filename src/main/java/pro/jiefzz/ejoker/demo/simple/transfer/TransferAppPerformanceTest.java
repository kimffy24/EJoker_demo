package pro.jiefzz.ejoker.demo.simple.transfer;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandReturnType;
import com.jiefzz.ejoker.eventing.DomainEventStream;
import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.infrastructure.ITypeNameProvider;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.DepositTransaction;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.CountSyncHelper;
import pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers.SyncHelper;

public class TransferAppPerformanceTest {
	
	private final static Logger logger = LoggerFactory.getLogger(TransferAppPerformanceTest.class);
	
	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		eJokerFrameworkInitializer.initAll();
		
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
            	commandService.executeAsync(new CreateAccountCommand(accountId, "SampleAccount" + j), CommandReturnType.EventHandled).get();
            	accountList[j] = accountId;
        	});
        }
        for(int i=0; i<accountCount; i++) {
        	if(null == accountList[i]) {
        		i--;
        		SleepWrapper.sleep(TimeUnit.MILLISECONDS, 0l);
        	}
        }
        
        logger.error("账号创建 OK.");

        countSyncHelper.setExpectedCount(accountCount);
        //每个账户都存入初始额度
        for(String accountId:accountList) {
        	StartDepositTransactionCommand cmd = new StartDepositTransactionCommand(ObjectId.get().toHexString(), accountId, depositAmount);
        	systemAsyncHelper.submit(() -> commandService.sendAsync(cmd).get());
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

        SleepWrapper.sleep(TimeUnit.SECONDS, 1l);

        logger.info("All OK.");

	}
}
