package pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 转账交易已开始
 * @author kimffy
 *
 */
public class TransferTransactionStartedEvent extends AbstractTransferTransactionEvent {

	public TransferTransactionStartedEvent() {
	}
	
	public TransferTransactionStartedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
