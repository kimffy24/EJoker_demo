package pro.jiefzz.ejoker_demo.transfer.processManagers;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.commanding.ICommandService;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.MessageHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapper;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.SystemFutureWrapperUtil;
import pro.jiefzz.ejoker.z.task.AsyncTaskResult;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.TransactionType;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionPreparationCompletedEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents.DepositTransactionStartedEvent;

@MessageHandler
@EService
public class DepositTransactionProcessManager extends AbstractMessageHandler {

	@Dependence
	private ICommandService commandService;
	
	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionStartedEvent evnt) {
		AddTransactionPreparationCommand cmd = new AddTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId(), TransactionType.DepositTransaction, PreparationType.CreditPreparation,
				evnt.getAmount());
		cmd.setId(evnt.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationAddedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositPreparationCommand command = new ConfirmDepositPreparationCommand(
					evnt.getTransactionPreparation().getTransactionId());
			command.setId(evnt.getId());
			return commandService.sendAsync(command);
		}
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DepositTransactionPreparationCompletedEvent evnt) {
		CommitTransactionPreparationCommand cmd = new CommitTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId());
		cmd.setId(evnt.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositCommand command = new ConfirmDepositCommand(
					evnt.getTransactionPreparation().getTransactionId());
			command.setId(evnt.getId());
			return commandService.sendAsync(command);
		}
		return SystemFutureWrapperUtil.completeFutureTask();
	}
}
