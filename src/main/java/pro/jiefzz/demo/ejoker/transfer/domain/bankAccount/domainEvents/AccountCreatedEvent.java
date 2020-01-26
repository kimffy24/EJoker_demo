package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.domainEvents;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

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
