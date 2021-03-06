package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jk.ejoker.commanding.AbstractCommand;

/**
 * 确认账户验证已通过
 * @author kimffy
 *
 */
public class ConfirmAccountValidatePassedCommand extends AbstractCommand<String> {

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
