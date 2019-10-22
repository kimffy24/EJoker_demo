package pro.jiefzz.eden.multiMessage.bus;

import pro.jiefzz.ejoker.eventing.AbstractDomainEvent;

public class DE5 extends AbstractDomainEvent<String> {

	public String name = "DE5";
	
	public DE5() {
		super();
	}

	public DE5(String name) {
		this();
		this.name = name;
	}
	
	
}
