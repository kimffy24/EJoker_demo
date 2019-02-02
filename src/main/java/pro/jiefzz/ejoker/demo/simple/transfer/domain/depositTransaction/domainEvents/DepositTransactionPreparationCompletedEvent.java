package pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

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
