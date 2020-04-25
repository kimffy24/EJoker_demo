package pro.jiefzz.eden.multiMessage.bus;

import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class DE6 extends AbstractDomainEvent<String> {

	public String name = "DE6";
	
	public DE6() {
		super();
	}

	public DE6(String name) {
		this();
		this.name = name;
	}
	
	
}
