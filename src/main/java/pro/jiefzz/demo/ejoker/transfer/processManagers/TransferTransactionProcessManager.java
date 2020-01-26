package pro.jiefzz.demo.ejoker.transfer.processManagers;

import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.ValidateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.CancelTransferTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmAccountValidatePassedCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferInCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferInPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferOutCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferOutPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.TransactionPreparation;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions.InsufficientBalanceException;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.AccountValidatePassedConfirmCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferInPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferOutPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionStartedEvent;
import pro.jiefzz.ejoker.commanding.ICommandService;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.enhance.ForEachUtil;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskStatus;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class TransferTransactionProcessManager extends AbstractMessageHandler {

	@Dependence
	private ICommandService commandService;

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransferTransactionStartedEvent evnt) {
		TransferTransactionInfo transactionInfo = evnt.getTransactionInfo();

		ValidateAccountCommand validateSourceAccountCommand = new ValidateAccountCommand(
				transactionInfo.getSourceAccountId(), evnt.getAggregateRootId());
		validateSourceAccountCommand.setId(evnt.getId());
		Future<AsyncTaskResult<Void>> sendAsync1 = commandService.sendAsync(validateSourceAccountCommand);

		ValidateAccountCommand validateTargetAccountCommand = new ValidateAccountCommand(
				transactionInfo.getTargetAccountId(), evnt.getAggregateRootId());
		validateTargetAccountCommand.setId(evnt.getId());
		Future<AsyncTaskResult<Void>> sendAsync2 = commandService.sendAsync(validateTargetAccountCommand);

		// 没用CompletableFuture，所以没有相关的串联的状态判断和等待
		AsyncTaskResult<Void> r1 = await(sendAsync1);
		AsyncTaskResult<Void> r2 = await(sendAsync2);

		AtomicBoolean faild = new AtomicBoolean(false);
		AtomicBoolean ioException = new AtomicBoolean(false);
		StringBuffer eMessageSb = new StringBuffer();
		ForEachUtil.processForEach(new AsyncTaskResult[] { r1, r2 }, r -> {
			if (AsyncTaskStatus.Failed.equals(r.getStatus())) {
				faild.set(true);
				eMessageSb.append(r.getErrorMessage());
				eMessageSb.append("| ");
			}
			if (AsyncTaskStatus.IOException.equals(r.getStatus())) {
				ioException.set(true);
				eMessageSb.append(r.getErrorMessage());
				eMessageSb.append("| ");
			}
		});
		if (faild.get()) {
			return EJokerFutureTaskUtil.newFutureTask(AsyncTaskStatus.Failed, eMessageSb.toString());
		}
		if (ioException.get()) {
			return EJokerFutureTaskUtil.newFutureTask(AsyncTaskStatus.IOException, eMessageSb.toString());
		}

		return EJokerFutureTaskUtil.completeTask();
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(AccountValidatePassedMessage message) {
		ConfirmAccountValidatePassedCommand cmd = new ConfirmAccountValidatePassedCommand(message.getTransactionId(),
				message.getAccountId());
		cmd.setId(message.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(AccountValidateFailedMessage message) {
		CancelTransferTransactionCommand cmd = new CancelTransferTransactionCommand(message.getTransactionId());
		cmd.setId(message.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(AccountValidatePassedConfirmCompletedEvent evnt) {
		AddTransactionPreparationCommand cmd = new AddTransactionPreparationCommand(
				evnt.getTransactionInfo().getSourceAccountId(), evnt.getAggregateRootId(),
				TransactionType.TransferTransaction, PreparationType.DebitPreparation,
				evnt.getTransactionInfo().getAmount());
		cmd.setId(evnt.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransactionPreparationAddedEvent evnt) {
		TransactionPreparation transactionPreparation = evnt.getTransactionPreparation();
		if (TransactionType.TransferTransaction.equals(transactionPreparation.getTransactionType())) {
			if (PreparationType.DebitPreparation.equals(transactionPreparation.getPreparationType())) {
				ConfirmTransferOutPreparationCommand cmd = new ConfirmTransferOutPreparationCommand(
						transactionPreparation.getTransactionId());
				cmd.setId(evnt.getId());
				return commandService.sendAsync(cmd);
			} else if (PreparationType.CreditPreparation.equals(transactionPreparation.getPreparationType())) {
				ConfirmTransferInPreparationCommand cmd = new ConfirmTransferInPreparationCommand(
						transactionPreparation.getTransactionId());
				cmd.setId(evnt.getId());
				return commandService.sendAsync(cmd);
			}
		}

		return EJokerFutureTaskUtil.completeTask();

	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(InsufficientBalanceException exception) {
		if (TransactionType.TransferTransaction.equals(exception.getTransactionType())) {
			CancelTransferTransactionCommand cmd = new CancelTransferTransactionCommand(exception.getTransactionId());
			cmd.setId(exception.getId());
			return commandService.sendAsync(cmd);
		}
		return EJokerFutureTaskUtil.completeTask();
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransferOutPreparationConfirmedEvent evnt) {
		AddTransactionPreparationCommand cmd = new AddTransactionPreparationCommand(
				evnt.getTransactionInfo().getTargetAccountId(), evnt.getAggregateRootId(),
				TransactionType.TransferTransaction, PreparationType.CreditPreparation,
				evnt.getTransactionInfo().getAmount());
		cmd.setId(evnt.getId());
		return commandService.sendAsync(cmd);
	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransferInPreparationConfirmedEvent evnt) {

		TransferTransactionInfo transactionInfo = evnt.getTransactionInfo();

		CommitTransactionPreparationCommand cmd1 = new CommitTransactionPreparationCommand(
				transactionInfo.getSourceAccountId(), evnt.getAggregateRootId());
		cmd1.setId(evnt.getId());
		Future<AsyncTaskResult<Void>> sendAsync1 = commandService.sendAsync(cmd1);

		CommitTransactionPreparationCommand cmd2 = new CommitTransactionPreparationCommand(
				transactionInfo.getTargetAccountId(), evnt.getAggregateRootId());
		cmd2.setId(evnt.getId());
		Future<AsyncTaskResult<Void>> sendAsync2 = commandService.sendAsync(cmd2);

		// 没用CompletableFuture，所以没有相关的串联的状态判断和等待
		AsyncTaskResult<Void> r1 = await(sendAsync1);
		AsyncTaskResult<Void> r2 = await(sendAsync2);

		AtomicBoolean faild = new AtomicBoolean(false);
		AtomicBoolean ioException = new AtomicBoolean(false);
		StringBuffer eMessageSb = new StringBuffer();
		ForEachUtil.processForEach(new AsyncTaskResult[] { r1, r2 }, r -> {
			if (AsyncTaskStatus.Failed.equals(r.getStatus())) {
				faild.set(true);
				eMessageSb.append(r.getErrorMessage());
				eMessageSb.append("| ");
			}
			if (AsyncTaskStatus.IOException.equals(r.getStatus())) {
				ioException.set(true);
				eMessageSb.append(r.getErrorMessage());
				eMessageSb.append("| ");
			}
		});
		if (faild.get()) {
			return EJokerFutureTaskUtil.newFutureTask(AsyncTaskStatus.Failed, eMessageSb.toString());
		}
		if (ioException.get()) {
			return EJokerFutureTaskUtil.newFutureTask(AsyncTaskStatus.IOException, eMessageSb.toString());
		}

		return EJokerFutureTaskUtil.completeTask();

	}

	@Suspendable
	public Future<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		TransactionPreparation transactionPreparation = evnt.getTransactionPreparation();
		if (TransactionType.TransferTransaction.equals(transactionPreparation.getTransactionType())) {
			if (PreparationType.DebitPreparation.equals(transactionPreparation.getPreparationType())) {
				ConfirmTransferOutCommand cmd = new ConfirmTransferOutCommand(
						transactionPreparation.getTransactionId());
				cmd.setId(evnt.getId());
				return commandService.sendAsync(cmd);
			} else if (PreparationType.CreditPreparation.equals(transactionPreparation.getPreparationType())) {
				ConfirmTransferInCommand cmd = new ConfirmTransferInCommand(transactionPreparation.getTransactionId());
				cmd.setId(evnt.getId());
				return commandService.sendAsync(cmd);
			}
		}
		return EJokerFutureTaskUtil.completeTask();
	}
}