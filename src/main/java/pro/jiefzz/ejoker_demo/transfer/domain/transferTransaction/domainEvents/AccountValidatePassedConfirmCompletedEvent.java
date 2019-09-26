package pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.domainEvents;

import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 源账户和目标账户验证通过事件都已确认
 * @author kimffy
 *
 */
public class AccountValidatePassedConfirmCompletedEvent extends AbstractTransferTransactionEvent {

	public AccountValidatePassedConfirmCompletedEvent() {
	}

	public AccountValidatePassedConfirmCompletedEvent(TransferTransactionInfo transactionInfo) {
		super(transactionInfo);
	}

}
