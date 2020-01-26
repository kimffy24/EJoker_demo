package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;

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
