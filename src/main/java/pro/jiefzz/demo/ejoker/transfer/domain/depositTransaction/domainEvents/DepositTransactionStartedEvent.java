package pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DepositTransactionStartedEvent extends AbstractDomainEvent<String> {

	private String accountId;
	
	private double amount;

	public DepositTransactionStartedEvent() {
		
	}
	
	public DepositTransactionStartedEvent(String accountId, double amount) {
		this.accountId = accountId;
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public double getAmount() {
		return amount;
	}
	
}
