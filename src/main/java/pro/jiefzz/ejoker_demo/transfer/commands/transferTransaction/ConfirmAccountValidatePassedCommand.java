package pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认账户验证已通过
 * @author kimffy
 *
 */
public class ConfirmAccountValidatePassedCommand extends AbstractCommand {

	private String accountId;

	public ConfirmAccountValidatePassedCommand() {
	}

	public ConfirmAccountValidatePassedCommand(String transactionId, String accountId) {
		super(transactionId);
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
}
