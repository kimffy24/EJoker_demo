package pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.domainEvents;

import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class DepositTransactionPreparationCompletedEvent extends AbstractDomainEvent<String> {

	private String accountId;
	
	public DepositTransactionPreparationCompletedEvent() {
		
	}
	
	public DepositTransactionPreparationCompletedEvent(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
}
