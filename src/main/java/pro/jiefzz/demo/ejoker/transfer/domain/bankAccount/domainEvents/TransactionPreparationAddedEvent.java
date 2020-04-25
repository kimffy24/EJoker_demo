package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.TransactionPreparation;
import pro.jk.ejoker.eventing.AbstractDomainEvent;

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
