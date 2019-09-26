package pro.jiefzz.ejoker_demo.transfer.commands.bankAccount;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class ValidateAccountCommand extends AbstractCommand {

	private String transactionId;

	public ValidateAccountCommand() {
	}

	public ValidateAccountCommand(String accountId, String transactionId) {
		super(accountId);
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

}
