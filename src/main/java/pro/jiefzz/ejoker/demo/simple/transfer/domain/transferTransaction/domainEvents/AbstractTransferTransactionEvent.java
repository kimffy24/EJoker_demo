package pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;

public abstract class AbstractTransferTransactionEvent extends AbstractDomainEvent<String> {

	private TransferTransactionInfo transactionInfo;
	
    public AbstractTransferTransactionEvent() { }
    public AbstractTransferTransactionEvent(TransferTransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }
    
	public TransferTransactionInfo getTransactionInfo() {
		return transactionInfo;
	}
    
}
