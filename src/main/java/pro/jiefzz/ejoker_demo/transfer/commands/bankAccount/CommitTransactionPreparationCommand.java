package pro.jiefzz.ejoker_demo.transfer.commands.bankAccount;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class CommitTransactionPreparationCommand extends AbstractCommand<String> {

	public String transactionId;

	public CommitTransactionPreparationCommand() {
	}

	public CommitTransactionPreparationCommand(String accountId, String transactionId) {
		super(accountId);
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}
	
}
