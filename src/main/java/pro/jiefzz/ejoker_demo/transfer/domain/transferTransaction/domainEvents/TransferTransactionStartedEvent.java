package pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

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
