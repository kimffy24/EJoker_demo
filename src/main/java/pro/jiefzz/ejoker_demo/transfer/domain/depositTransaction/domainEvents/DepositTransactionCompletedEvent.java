package pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.domainEvents;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DepositTransactionCompletedEvent extends AbstractDomainEvent<String> {

	private String accountId;
	
	public DepositTransactionCompletedEvent() {
		
	}
	
	public DepositTransactionCompletedEvent(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
}
