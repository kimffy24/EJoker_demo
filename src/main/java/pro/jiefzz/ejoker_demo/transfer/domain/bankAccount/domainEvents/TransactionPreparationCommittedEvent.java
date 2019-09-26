package pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.TransactionPreparation;

public class TransactionPreparationCommittedEvent extends AbstractDomainEvent<String> {

	private double currentBalance;
	
	private TransactionPreparation transactionPreparation;

	public TransactionPreparationCommittedEvent() {
	}

	public TransactionPreparationCommittedEvent(double currentBalance, TransactionPreparation transactionPreparation) {
		this.currentBalance = currentBalance;
		this.transactionPreparation = transactionPreparation;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public TransactionPreparation getTransactionPreparation() {
		return transactionPreparation;
	}
	
	
}
