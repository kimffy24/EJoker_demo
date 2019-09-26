package pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 目标账户验证通过事件已确认
 * @author kimffy
 *
 */
public class TargetAccountValidatePassedConfirmedEvent extends AbstractTransferTransactionEvent {

	public TargetAccountValidatePassedConfirmedEvent() {
	}

	public TargetAccountValidatePassedConfirmedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}