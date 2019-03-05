package pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 转账交易预转入已确认
 * @author kimffy
 *
 */
public class TransferInPreparationConfirmedEvent extends AbstractTransferTransactionEvent {

	public TransferInPreparationConfirmedEvent() {
	}

	public TransferInPreparationConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
