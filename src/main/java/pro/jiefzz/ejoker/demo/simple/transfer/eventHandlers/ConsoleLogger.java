package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.ejoker.demo.simple.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.AccountCreatedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.exceptions.InsufficientBalanceException;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferInPreparationConfirmedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferOutPreparationConfirmedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionStartedEvent;

@MessageHandler
@EService
public class ConsoleLogger extends AbstractMessageHandler {

	private final static Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);

	@Dependence
	IJSONConverter jsonConverter;

	//// for debug
	private AtomicInteger accountCreatedEventHit = new AtomicInteger(0);
	private AtomicInteger transferTransactionCompletedEventHit = new AtomicInteger(0);
	
	public int getAccountHit() {
		return accountCreatedEventHit.get();
	}

	public void show() {
		logger.error("收到的账户成功创建事件的总数: {}", accountCreatedEventHit.get());
		logger.error("收到的转账完成事件事件的总数: {}", transferTransactionCompletedEventHit.get());
	}
	//// for debug

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(AccountCreatedEvent message) {
		logger.info("创建账号成功: 账户={}, 所有者={}, 当前时间戳={}", message.getAggregateRootId(), message.getOwner(), System.currentTimeMillis());
		{ /// for debug
			accountCreatedEventHit.incrementAndGet();
		}
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(AccountValidatePassedMessage message) {
		logger.info("账户验证已通过，交易ID：{}，账户：{}", message.getTransactionId(), message.getAccountId());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(AccountValidateFailedMessage message) {
		logger.info("无效的银行账户，交易ID：{}，账户：{}，理由：{}", message.getTransactionId(), message.getAccountId(),
				message.getReason());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationAddedEvent evnt) {
		if (TransactionType.TransferTransaction.equals(evnt.getTransactionPreparation().getTransactionType())) {
			if (PreparationType.DebitPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
				logger.info("账户预转出成功，交易ID：{}，账户：{}，金额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount());
			} else if (PreparationType.CreditPreparation
					.equals(evnt.getTransactionPreparation().getPreparationType())) {
				logger.info("账户预转入成功，交易ID：{}，账户：{}，金额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount());
			}
		}

		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())) {
			if (PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
				logger.info("账户存款已成功，账户：{}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getAccountId(),
						evnt.getTransactionPreparation().getAmount(), evnt.getCurrentBalance());
			}
		}
		if (TransactionType.TransferTransaction.equals(evnt.getTransactionPreparation().getTransactionType())) {
			if (PreparationType.DebitPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
				logger.info("账户转出已成功，交易ID：{}，账户：{}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount(),
						evnt.getCurrentBalance());
			}
			if (PreparationType.CreditPreparation.equals(evnt.getTransactionPreparation().getPreparationType())) {
				logger.info("账户转入已成功，交易ID：{}，账户：{}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount(),
						evnt.getCurrentBalance());
			}
		}

		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionStartedEvent evnt) {
		TransferTransactionInfo transactionInfo = evnt.getTransactionInfo();
		logger.info("转账交易已开始，交易ID：{}，源账户：{}，目标账户：{}，转账金额：{}", evnt.getAggregateRootId(),
				transactionInfo.getSourceAccountId(), transactionInfo.getTargetAccountId(),
				transactionInfo.getAmount());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferOutPreparationConfirmedEvent evnt) {
		logger.info("预转出确认成功，交易ID：{}，账户：{}", evnt.getAggregateRootId(), evnt.getTransactionInfo().getSourceAccountId());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferInPreparationConfirmedEvent evnt) {
		logger.info("预转入确认成功，交易ID：{}，账户：{}", evnt.getAggregateRootId(), evnt.getTransactionInfo().getTargetAccountId());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCompletedEvent evnt) {
		logger.info("转账交易已完成，交易ID：{}", evnt.getAggregateRootId());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(InsufficientBalanceException exception) {
		{ /// for debug
			transferTransactionCompletedEventHit.incrementAndGet();
		}
		logger.info("账户的余额不足，交易ID：{}，账户：{}，可用余额：{}，转出金额：{}", exception.getTransactionId(), exception.getAccountId(),
				exception.getCurrentAvailableBalance(), exception.getAmount());
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCanceledEvent evnt) {
		logger.info("转账交易已取消，交易ID：{}", evnt.getAggregateRootId());
		return SystemFutureWrapperUtil.completeFutureTask();
	}
}
