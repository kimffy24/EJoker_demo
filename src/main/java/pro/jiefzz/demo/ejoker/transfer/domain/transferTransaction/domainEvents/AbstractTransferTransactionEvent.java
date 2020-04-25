package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jk.ejoker.eventing.AbstractDomainEvent;

public abstract class AbstractTransferTransactionEvent extends AbstractDomainEvent<String> {

	private TransferTransactionInfo transactionInfo;
	
    public AbstractTransferTransactionEvent() {
    }
    
    public AbstractTransferTransactionEvent(TransferTransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }
    
	public TransferTransactionInfo getTransactionInfo() {
		return transactionInfo;
	}
    
}
