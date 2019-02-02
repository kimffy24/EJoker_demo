package pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

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
