package pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

public class StartDepositTransactionCommand extends AbstractCommand {

	private String accountId;

	private double amount;

	public StartDepositTransactionCommand() {

	}

	public StartDepositTransactionCommand(String transactionId, String accountId, double amount) {
		super(transactionId);
		this.accountId = accountId;
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
