package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 转账交易转入已确认
 * @author kimffy
 *
 */
public class TransferInConfirmedEvent extends AbstractTransferTransactionEvent {

	public TransferInConfirmedEvent() {
	}

	public TransferInConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
