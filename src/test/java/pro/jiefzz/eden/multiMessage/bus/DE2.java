package pro.jiefzz.eden.multiMessage.bus;

import pro.jk.ejoker.eventing.AbstractDomainEvent;

public class DE2 extends AbstractDomainEvent<String> {

	public String name = "DE2";
	
	public DE2() {
		super();
	}

	public DE2(String name) {
		this();
		this.name = name;
	}
	
	
}
