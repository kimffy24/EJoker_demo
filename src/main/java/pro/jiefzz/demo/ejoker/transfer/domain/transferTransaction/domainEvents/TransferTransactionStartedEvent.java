package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;

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
