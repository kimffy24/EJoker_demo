package pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.TransactionPreparation;

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