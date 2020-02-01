package pro.jiefzz.demo.ejoker.transfer.processManagers;

import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionPreparationCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionStartedEvent;
import pro.jiefzz.ejoker.commanding.ICommandService;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class DepositTransactionProcessManager extends AbstractMessageHandler {

	@Dependence
	private ICommandService commandService;
	
	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(DepositTransactionStartedEvent evnt) {
		AddTransactionPreparationCommand cmd = new AddTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId(), TransactionType.DepositTransaction, PreparationType.CreditPreparation,
				evnt.getAmount());
		cmd.setId(evnt.getId());
		cmd.setItems(evnt.getItems());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransactionPreparationAddedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositPreparationCommand cmd = new ConfirmDepositPreparationCommand(
					evnt.getTransactionPreparation().getTransactionId());
			cmd.setId(evnt.getId());
			cmd.setItems(evnt.getItems());
			return commandService.sendAsync(cmd);
		}
		return EJokerFutureTaskUtil.completeTask();
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(DepositTransactionPreparationCompletedEvent evnt) {
		CommitTransactionPreparationCommand cmd = new CommitTransactionPreparationCommand(evnt.getAccountId(),
				evnt.getAggregateRootId());
		cmd.setId(evnt.getId());
		cmd.setItems(evnt.getItems());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())
				&& PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
			ConfirmDepositCommand cmd = new ConfirmDepositCommand(
					evnt.getTransactionPreparation().getTransactionId());
			cmd.setId(evnt.getId());
			cmd.setItems(evnt.getItems());
			return commandService.sendAsync(cmd);
		}
		return EJokerFutureTaskUtil.completeTask();
	}
}
