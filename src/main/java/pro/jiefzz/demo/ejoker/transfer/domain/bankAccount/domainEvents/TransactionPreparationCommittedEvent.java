package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.TransactionPreparation;
import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

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
