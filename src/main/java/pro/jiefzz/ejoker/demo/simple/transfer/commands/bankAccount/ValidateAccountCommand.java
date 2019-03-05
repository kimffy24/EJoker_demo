package pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount;

import com.jiefzz.ejoker.commanding.AbstractCommand;

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
