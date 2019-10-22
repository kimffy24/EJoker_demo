package pro.jiefzz.eden.multiMessage.bus;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DE8 extends AbstractDomainEvent<String> {

	public String name = "DE8";
	
	public DE8() {
		super();
	}

	public DE8(String name) {
		this();
		this.name = name;
	}
	
	
}
