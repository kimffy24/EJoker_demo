package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.TransactionPreparation;

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
