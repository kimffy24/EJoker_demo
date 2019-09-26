package pro.jiefzz.ejoker_demo.transfer.commands.bankAccount;

import pro.jiefzz.ejoker.commanding.AbstractCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.TransactionType;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.PreparationType;

public class AddTransactionPreparationCommand extends AbstractCommand {

	private String transactionId;

	private TransactionType transactionType;
	
	private PreparationType preparationType;
	
	private double amount;

	public AddTransactionPreparationCommand() {
	}

	public AddTransactionPreparationCommand(String accountId, String transactionId, TransactionType transactionType,
			PreparationType preparationType, double amount) {
		super(accountId);
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.preparationType = preparationType;
		this.amount = amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public PreparationType getPreparationType() {
		return preparationType;
	}

	public double getAmount() {
		return amount;
	}
	
}
