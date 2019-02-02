package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.TransactionPreparation;

public class TransactionPreparationAddedEvent extends AbstractDomainEvent<String> {
	
	private TransactionPreparation transactionPreparation;

    public TransactionPreparationAddedEvent() {
    }
    
    public TransactionPreparationAddedEvent(TransactionPreparation transactionPreparation) {
        this.transactionPreparation = transactionPreparation;
    }

	public TransactionPreparation getTransactionPreparation() {
		return transactionPreparation;
	}
    
}
