package pro.jiefzz.ejoker.demo.simple.transfer.processManagers;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.ICommandService;
import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.task.AsyncTaskStatus;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionPreparationCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionStartedEvent;

@MessageHandler
@EService
public class DepositTransactionProcessManager extends AbstractMessageHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(DepositTransactionProcessManager.class);

	@Dependence
	SystemAsyncHelper asyncHelper;

	@Dependence
	private ICommandService commandService;
	
	AtomicLong al1 = new AtomicLong(0);
	AtomicLong al2 = new AtomicLong(0);
	AtomicLong al3 = new AtomicLong(0);
	AtomicLong al4 = new AtomicLong(0);
	AtomicLong al5 = new AtomicLong(0);
	AtomicLong al6 = new AtomicLong(0);
	AtomicLong al7 = new AtomicLong(0);
	AtomicLong al8 = new AtomicLong(0);

	public void show1() {
		logger.error("DepositTransactionStartedEvent hit: {}", al1.get());
		logger.error("TransactionPreparationAddedEvent hit: {}", al2.get());
		logger.error("DepositTransactionPreparationCompletedEvent hit: {}", al3.get());
		logger.error("TransactionPreparationCommittedEvent hit: {}", al4.get());
	}

	public void show2() {
		logger.error("AddTransactionPreparationCommand send: {}", al5.get());
		logger.error("ConfirmDepositPreparationCommand send: {}", al6.get());
		logger.error("CommitTransactionPreparationCommand send: {}", al7.get());
		logger.error("ConfirmDepositCommand send: {} \n", al8.get());
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionStartedEvent evnt) {
		al1.getAndIncrement();
		AddTransactionPreparationCommand cmd = new AddTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId(), TransactionType.DepositTransaction, PreparationType.CreditPreparation,
				evnt.getAmount());
		cmd.setId(evnt.getId());
		SystemFutureWrapper<AsyncTaskResult<Void>> sendAsync = commandService.sendAsync(cmd);
		asyncHelper.submit(() -> {
			AsyncTaskResult<Void> asyncTaskResult = sendAsync.get();
			if(AsyncTaskStatus.Success.equals(asyncTaskResult.getStatus()))
				al5.getAndIncrement();
				;
		});
		return sendAsync;
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationAddedEvent evnt) {
		al2.getAndIncrement();
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositPreparationCommand command = new ConfirmDepositPreparationCommand(
					evnt.getTransactionPreparation().getTransactionId());
			command.setId(evnt.getId());
			SystemFutureWrapper<AsyncTaskResult<Void>> sendAsync = commandService.sendAsync(command);
			asyncHelper.submit(() -> {
				AsyncTaskResult<Void> asyncTaskResult = sendAsync.get();
				if(AsyncTaskStatus.Success.equals(asyncTaskResult.getStatus()))
					al6.getAndIncrement();
					;
			});
			return sendAsync;
		}
		
		return SystemFutureWrapperUtil.createCompleteFuture(null);
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionPreparationCompletedEvent evnt) {
		al3.getAndIncrement();
		CommitTransactionPreparationCommand cmd = new CommitTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId());
		cmd.setId(evnt.getId());
		SystemFutureWrapper<AsyncTaskResult<Void>> sendAsync = commandService.sendAsync(cmd);
		asyncHelper.submit(() -> {
			AsyncTaskResult<Void> asyncTaskResult = sendAsync.get();
			if(AsyncTaskStatus.Success.equals(asyncTaskResult.getStatus()))
				al7.getAndIncrement();
				;
		});
		return sendAsync;
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		al4.getAndIncrement();
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositCommand command = new ConfirmDepositCommand(
					evnt.getTransactionPreparation().getTransactionId());
			command.setId(evnt.getId());
			SystemFutureWrapper<AsyncTaskResult<Void>> sendAsync = commandService.sendAsync(command);
			asyncHelper.submit(() -> {
				AsyncTaskResult<Void> asyncTaskResult = sendAsync.get();
				if(AsyncTaskStatus.Success.equals(asyncTaskResult.getStatus()))
					al8.getAndIncrement();
					;
			});
			return sendAsync;
		}

		return SystemFutureWrapperUtil.createCompleteFuture(null);
	}
}
