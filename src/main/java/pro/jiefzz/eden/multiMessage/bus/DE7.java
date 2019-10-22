package pro.jiefzz.eden.multiMessage.bus;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DE7 extends AbstractDomainEvent<String> {

	public String name = "DE7";
	
	public DE7() {
		super();
	}

	public DE7(String name) {
		this();
		this.name = name;
	}
	
	
}
