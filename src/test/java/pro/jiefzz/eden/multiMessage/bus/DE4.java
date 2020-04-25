package pro.jiefzz.eden.multiMessage.bus;

import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class DE4 extends AbstractDomainEvent<String> {

	public String name = "DE4";
	
	public DE4() {
		super();
	}

	public DE4(String name) {
		this();
		this.name = name;
	}
	
	
}
