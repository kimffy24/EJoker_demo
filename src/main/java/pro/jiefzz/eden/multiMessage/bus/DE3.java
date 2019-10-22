package pro.jiefzz.eden.multiMessage.bus;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DE3 extends AbstractDomainEvent<String> {

	public String name = "DE3";
	
	public DE3() {
		super();
	}

	public DE3(String name) {
		this();
		this.name = name;
	}
	
	
}
