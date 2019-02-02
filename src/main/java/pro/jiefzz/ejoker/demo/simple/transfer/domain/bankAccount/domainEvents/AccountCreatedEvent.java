package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.domainEvents;

import com.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class AccountCreatedEvent extends AbstractDomainEvent<String> {

	private String owner;

	public AccountCreatedEvent() {
		
	}
	
	public AccountCreatedEvent(String owner){
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}
	
}
