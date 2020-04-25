package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.TransactionPreparation;
import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class TransactionPreparationCanceledEvent extends AbstractDomainEvent<String> {

	private TransactionPreparation transactionPreparation;

    public TransactionPreparationCanceledEvent() {
    }
    
    public TransactionPreparationCanceledEvent(TransactionPreparation transactionPreparation) {
        this.transactionPreparation = transactionPreparation;
    }

	public TransactionPreparation getTransactionPreparation() {
		return transactionPreparation;
	}
}
