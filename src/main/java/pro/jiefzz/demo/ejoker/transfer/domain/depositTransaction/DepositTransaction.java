package pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionStatus;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionPreparationCompletedEvent;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents.DepositTransactionStartedEvent;
import pro.jiefzz.ejoker.common.context.annotation.assemblies.AggregateRoot;
import pro.jiefzz.ejoker.domain.AbstractAggregateRoot;

/**
 * 聚合根，表示一笔银行存款交易
 * @author kimffy
 *
 */
@AggregateRoot
public class DepositTransaction extends AbstractAggregateRoot<String> {

	private String accountId;

	@SuppressWarnings("unused")
	private double amount;

	private TransactionStatus status;

	public DepositTransaction() {
	}
	
	public DepositTransaction(String transactionId, String accountId, double amount)  {
		super(transactionId);
		applyEvent(new DepositTransactionStartedEvent(accountId, amount));
	}
	
	/**
	 * 确认预存款
	 */
    public void confirmDepositPreparation() {
        if (TransactionStatus.Started.equals(status)) {
            applyEvent(new DepositTransactionPreparationCompletedEvent(accountId));
        }
    }
    
    /**
     * 确认存款
     */
    public void confirmDeposit() {
        if (TransactionStatus.PreparationCompleted.equals(status)) {
            applyEvent(new DepositTransactionCompletedEvent(accountId));
        }
    }

	// =========== handler ===========

    @SuppressWarnings("unused")
    private void handle(DepositTransactionStartedEvent evnt) {
        accountId = evnt.getAccountId();
        amount = evnt.getAmount();
        status = TransactionStatus.Started;
    }

    @SuppressWarnings("unused")
    private void handle(DepositTransactionPreparationCompletedEvent evnt) {
        status = TransactionStatus.PreparationCompleted;
    }
    
    @SuppressWarnings("unused")
	private void handle(DepositTransactionCompletedEvent evnt) {
        status = TransactionStatus.Completed;
    }
}
