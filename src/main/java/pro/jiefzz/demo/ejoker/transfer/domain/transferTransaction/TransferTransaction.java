package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionStatus;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.AccountValidatePassedConfirmCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.SourceAccountValidatePassedConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TargetAccountValidatePassedConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferInConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferInPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferOutConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferOutPreparationConfirmedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCanceledEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents.TransferTransactionStartedEvent;
import pro.jk.ejoker.common.context.annotation.assemblies.AggregateRoot;
import pro.jk.ejoker.domain.AbstractAggregateRoot;

/**
 * 聚合根，表示一笔银行内账户之间的转账交易
 * @author kimffy
 *
 */
@AggregateRoot
public class TransferTransaction extends AbstractAggregateRoot<String> {

	private TransferTransactionInfo transactionInfo;
	private TransactionStatus status;
	private boolean isSourceAccountValidatePassed;
	private boolean isTargetAccountValidatePassed;
	private boolean isTransferOutPreparationConfirmed;
	private boolean isTransferInPreparationConfirmed;
	private boolean isTransferOutConfirmed;
	private boolean isTransferInConfirmed;


	public TransferTransaction() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param transactionId
	 * @param transactionInfo
	 */
	public TransferTransaction(String transactionId, TransferTransactionInfo transactionInfo) {
		super(transactionId);
		applyEvent(new TransferTransactionStartedEvent(transactionInfo));
	}

	/**
	 * 确认账户验证通过
	 * 
	 * @param accountId
	 */
	public void confirmAccountValidatePassed(String accountId) {
		if (TransactionStatus.Started.equals(status)) {
			if (accountId.equals(transactionInfo.getSourceAccountId())) {
				if (!isSourceAccountValidatePassed) {
					applyEvent(new SourceAccountValidatePassedConfirmedEvent(transactionInfo));
					if (isTargetAccountValidatePassed) {
						applyEvent(new AccountValidatePassedConfirmCompletedEvent(transactionInfo));
					}
				}
			} else if (accountId.equals(transactionInfo.getTargetAccountId())) {
				if (!isTargetAccountValidatePassed) {
					applyEvent(new TargetAccountValidatePassedConfirmedEvent(transactionInfo));
					if (isSourceAccountValidatePassed) {
						applyEvent(new AccountValidatePassedConfirmCompletedEvent(transactionInfo));
					}
				}
			}
		}
	}

	/**
	 * 确认预转出
	 */
	public void confirmTransferOutPreparation() {
		if (TransactionStatus.AccountValidateCompleted.equals(status)) {
			if (!isTransferOutPreparationConfirmed) {
				applyEvent(new TransferOutPreparationConfirmedEvent(transactionInfo));
			}
		}
	}

	/**
	 * 确认预转入
	 */
	public void confirmTransferInPreparation() {
		if (TransactionStatus.AccountValidateCompleted.equals(status)) {
			if (!isTransferInPreparationConfirmed) {
				applyEvent(new TransferInPreparationConfirmedEvent(transactionInfo));
			}
		}
	}

	/**
	 * 确认转出
	 */
	public void confirmTransferOut() {
		if (TransactionStatus.PreparationCompleted.equals(status)) {
			if (!isTransferOutConfirmed) {
				applyEvent(new TransferOutConfirmedEvent(transactionInfo));
				if (isTransferInConfirmed) {
					applyEvent(new TransferTransactionCompletedEvent());
				}
			}
		}
	}

	/**
	 * 确认转入
	 */
	public void confirmTransferIn() {
		if (TransactionStatus.PreparationCompleted.equals(status)) {
			if (!isTransferInConfirmed) {
				applyEvent(new TransferInConfirmedEvent(transactionInfo));
				if (isTransferOutConfirmed) {
					applyEvent(new TransferTransactionCompletedEvent());
				}
			}
		}
	}

	/**
	 * 取消转账交易
	 */
	public void cancel() {
		applyEvent(new TransferTransactionCanceledEvent());
	}

	@SuppressWarnings("unused")
	private void handle(TransferTransactionStartedEvent evnt) {
		transactionInfo = evnt.getTransactionInfo();
		status = TransactionStatus.Started;
	}

	@SuppressWarnings("unused")
	private void handle(SourceAccountValidatePassedConfirmedEvent evnt) {
		isSourceAccountValidatePassed = true;
	}

	@SuppressWarnings("unused")
	private void handle(TargetAccountValidatePassedConfirmedEvent evnt) {
		isTargetAccountValidatePassed = true;
	}

	@SuppressWarnings("unused")
	private void handle(AccountValidatePassedConfirmCompletedEvent evnt) {
		status = TransactionStatus.AccountValidateCompleted;
	}

	@SuppressWarnings("unused")
	private void handle(TransferOutPreparationConfirmedEvent evnt) {
		isTransferOutPreparationConfirmed = true;
	}

	@SuppressWarnings("unused")
	private void handle(TransferInPreparationConfirmedEvent evnt) {
		isTransferInPreparationConfirmed = true;
		status = TransactionStatus.PreparationCompleted;
	}

	@SuppressWarnings("unused")
	private void handle(TransferOutConfirmedEvent evnt) {
		isTransferOutConfirmed = true;
	}

	@SuppressWarnings("unused")
	private void handle(TransferInConfirmedEvent evnt) {
		isTransferInConfirmed = true;
	}

	@SuppressWarnings("unused")
	private void handle(TransferTransactionCompletedEvent evnt) {
		status = TransactionStatus.Completed;
	}

	@SuppressWarnings("unused")
	private void handle(TransferTransactionCanceledEvent evnt) {
		status = TransactionStatus.Canceled;
	}

}
