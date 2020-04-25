package pro.jiefzz.eden.multiMessage.bus;

import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class DE1 extends AbstractDomainEvent<String> {

	public String name = "DE1";
	
	public DE1() {
		super();
	}

	public DE1(String name) {
		this();
		this.name = name;
	}
	
	
}
