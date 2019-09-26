package pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 源账户验证通过事件已确认
 * @author kimffy
 *
 */
public class SourceAccountValidatePassedConfirmedEvent extends AbstractTransferTransactionEvent {

	public SourceAccountValidatePassedConfirmedEvent() {
	}

	public SourceAccountValidatePassedConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}