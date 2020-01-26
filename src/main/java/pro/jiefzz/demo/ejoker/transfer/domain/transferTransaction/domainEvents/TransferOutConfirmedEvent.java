package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;

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
