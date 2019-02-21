package pro.jiefzz.ejoker.demo.simple.transfer.eventHandlers;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.InsufficientBalanceException;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.AccountCreatedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;

@MessageHandler
@EService
public class TestConsoleHelper extends AbstractMessageHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(TestConsoleHelper.class);

	@Dependence
	IJSONConverter jsonConverter;
	
	//// for debug
	private AtomicInteger accountAmount = new AtomicInteger(0);
	
	public void show() {
		logger.error("账号总数: {}", accountAmount.get());
	}
	//// for debug

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(AccountCreatedEvent message) {
		logger.info("创建账号成功: aggregateId={}, onwer={}, ", message.getAggregateRootId(), message.getOwner());
		{ /// for debug
			accountAmount.incrementAndGet();
		}
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

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

		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransactionPreparationCommittedEvent evnt) {
		if (TransactionType.DepositTransaction.equals(evnt.getTransactionPreparation().getTransactionType())) {
			if (evnt.getTransactionPreparation().getPreparationType() == PreparationType.CreditPreparation) {
				logger.info("账户存款已成功，账户：{}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getAccountId(),
						evnt.getTransactionPreparation().getAmount(), evnt.getCurrentBalance());
			}
		}
		if (evnt.getTransactionPreparation().getTransactionType() == TransactionType.TransferTransaction) {
			if (evnt.getTransactionPreparation().getPreparationType() == PreparationType.DebitPreparation) {
				logger.info("账户转出已成功，交易ID：{}，账户：{1}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount(),
						evnt.getCurrentBalance());
			}
			if (evnt.getTransactionPreparation().getPreparationType() == PreparationType.CreditPreparation) {
				logger.info("账户转入已成功，交易ID：{}，账户：{}，金额：{}，当前余额：{}", evnt.getTransactionPreparation().getTransactionId(),
						evnt.getTransactionPreparation().getAccountId(), evnt.getTransactionPreparation().getAmount(),
						evnt.getCurrentBalance());
			}
		}
		
		{ /// for debug
		}

		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCompletedEvent evnt) {
		logger.info("转账交易已完成，交易ID：{}", evnt.getAggregateRootId());

		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(InsufficientBalanceException exception) {
		logger.info("账户的余额不足，交易ID：{}，账户：{}，可用余额：{}，转出金额：{}", exception.getTransactionId(), exception.getAccountId(),
				exception.getCurrentAvailableBalance(), exception.getAmount());

		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(TransferTransactionCanceledEvent evnt) {
		logger.info("转账交易已取消，交易ID：{}", evnt.getAggregateRootId());

		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}
}
