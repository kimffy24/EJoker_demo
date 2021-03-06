package pro.jiefzz.demo.ejoker.transfer.commands.bankAccount;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.PreparationType;
import pro.jk.ejoker.commanding.AbstractCommand;

public class AddTransactionPreparationCommand extends AbstractCommand<String> {

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
