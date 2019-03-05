package pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 转账交易预转出已确认
 * @author kimffy
 *
 */
public class TransferOutPreparationConfirmedEvent extends AbstractTransferTransactionEvent {

	public TransferOutPreparationConfirmedEvent() {
	}

	public TransferOutPreparationConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
