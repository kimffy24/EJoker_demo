package pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 转账交易转出已确认
 * @author kimffy
 *
 */
public class TransferOutConfirmedEvent extends AbstractTransferTransactionEvent {

	public TransferOutConfirmedEvent() {
	}

	public TransferOutConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
