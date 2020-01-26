package pro.jiefzz.demo.ejoker.transfer.commands.bankAccount;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class ValidateAccountCommand extends AbstractCommand<String> {

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
