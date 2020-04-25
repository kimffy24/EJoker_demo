package pro.jiefzz.demo.ejoker.transfer.eventHandlers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.PreparationType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.AccountCreatedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationAddedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents.TransactionPreparationCommittedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions.InsufficientBalanceException;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferInPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferOutPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionStartedEvent;
import pro.jk.ejoker.common.context.annotation.context.Dependence;
import pro.jk.ejoker.common.context.annotation.context.ESType;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.service.IJSONConverter;
import pro.jk.ejoker.common.system.extension.acrossSupport.EJokerFutureUtil;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

@EService(type = ESType.MESSAGE_HANDLER)
public class ConsoleLogger extends AbstractMessageHandler {

	private final static Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);

	@Dependence
	private IJSONConverter jsonConverter;

	/// for debug

	private AtomicInteger accountCreatedEventHit = new AtomicInteger(0);
	private AtomicInteger depositTransactionCompletedEventHit = new AtomicInteger(0);
	private AtomicInteger transferTransactionCompletedEventHit = new AtomicInteger(0);
	
	public int getAccountHit() {
		return accountCreatedEventHit.get();
	}

	public void probe() {
		logger.error("收到的账户成功创建事件的总数: {}", accountCreatedEventHit.get());
		logger.error("收到的存款完成事件事件的总数: {}", depositTransactionCompletedEventHit.get());
		logger.error("收到的转账完成事件事件的总数: {}", transferTransactionCompletedEventHit.get());
	}
	
	private String latestTransferAccountId = "";
	private CountDownLatch cdlEnd = new CountDownLatch(1);
	
	public void awaitEnd() throws InterruptedException {
		cdlEnd.await();
	}

	public void setLatestTransferId(String latestTransferId) {
		this.latestTransferAccountId = latestTransferId;
	}
	
	/// for debug end

	@Suspendable
	public Future<Void> handleAsync(AccountCreatedEvent message) {
		logger.info("创建账号成功: 账户={}, 所有者={}, 当前时间戳={}", message.getAggregateRootId(), message.getOwner(), System.currentTimeMillis());
		{ /// for debug
			accountCreatedEventHit.incrementAndGet();
		}
		return EJokerFutureUtil.completeFuture();
	}
	
	public Future<Void> handleAsync(DepositTransactionCompletedEvent message) {
		{ /// for debug
			depositTransactionCompletedEventHit.incrementAndGet();
			if(latestTransferAccountId.equals(message.getAccountId())) {
				cdlEnd.countDown();
			}
		}
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(AccountValidatePassedMessage message) {
		logger.info("账户验证已通过，交易ID：{}，账户：{}", message.getTransactionId(), message.getAccountId());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(AccountValidateFailedMessage message) {
		logger.info("无效的银行账户，交易ID：{}，账户：{}，理由：{}", message.getTransactionId(), message.getAccountId(),
				message.getReason());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransactionPreparationAddedEvent evnt) {
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

		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransactionPreparationCommittedEvent evnt) {
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

		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionStartedEvent evnt) {
		TransferTransactionInfo transactionInfo = evnt.getTransactionInfo();
		logger.info("转账交易已开始，交易ID：{}，源账户：{}，目标账户：{}，转账金额：{}", evnt.getAggregateRootId(),
				transactionInfo.getSourceAccountId(), transactionInfo.getTargetAccountId(),
				transactionInfo.getAmount());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferOutPreparationConfirmedEvent evnt) {
		logger.info("预转出确认成功，交易ID：{}，账户：{}", evnt.getAggregateRootId(), evnt.getTransactionInfo().getSourceAccountId());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferInPreparationConfirmedEvent evnt) {
		logger.info("预转入确认成功，交易ID：{}，账户：{}", evnt.getAggregateRootId(), evnt.getTransactionInfo().getTargetAccountId());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionCompletedEvent evnt) {
		logger.info("转账交易已完成，交易ID：{}", evnt.getAggregateRootId());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(InsufficientBalanceException exception) {
		{ /// for debug
			transferTransactionCompletedEventHit.incrementAndGet();
		}
		logger.info("账户的余额不足，交易ID：{}，账户：{}，可用余额：{}，转出金额：{}", exception.getTransactionId(), exception.getAccountId(),
				exception.getCurrentAvailableBalance(), exception.getAmount());
		return EJokerFutureUtil.completeFuture();
	}

	@Suspendable
	public Future<Void> handleAsync(TransferTransactionCanceledEvent evnt) {
		logger.info("转账交易已取消，交易ID：{}", evnt.getAggregateRootId());
		return EJokerFutureUtil.completeFuture();
	}
}
