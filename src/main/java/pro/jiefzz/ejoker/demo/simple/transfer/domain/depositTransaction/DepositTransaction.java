package pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction;

import com.jiefzz.ejoker.domain.AbstractAggregateRoot;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.AggregateRoot;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionStatus;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionPreparationCompletedEvent;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents.DepositTransactionStartedEvent;

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
