package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jk.ejoker.domain.domainException.AbstractDomainException;

public class InsufficientBalanceException extends AbstractDomainException {

	private static final long serialVersionUID = 2101829149997987092L;

	private String accountId;
	
	private String transactionId;
	
	private TransactionType transactionType;
	
	private double amount;
	
	private double currentBalance;
	
	private double currentAvailableBalance;

	public InsufficientBalanceException() {
	}
	
	public InsufficientBalanceException(String accountId, String transactionId, TransactionType transactionType,
			double amount, double currentBalance, double currentAvailableBalance) {
		super("Insufficient Balance");
		this.accountId = accountId;
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.currentBalance = currentBalance;
		this.currentAvailableBalance = currentAvailableBalance;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public double getCurrentAvailableBalance() {
		return currentAvailableBalance;
	}
}
