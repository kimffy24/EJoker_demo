package pro.jiefzz.demo.ejoker.transfer.debug;

import pro.jk.ejoker.common.context.annotation.context.Dependence;
import pro.jk.ejoker.common.context.annotation.context.EInitialize;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.infrastructure.impl.AbstractMessageHandler;

public abstract class DAssemblier extends AbstractMessageHandler {

	@Dependence
	private IEJokerSimpleContext eJokerContext;
	
	private DevHelper devHelper = null;
	
	@EInitialize(priority = 99)
	private void initFather() {
		devHelper = DevHelper.of(eJokerContext);
		devHelper.connectField(this);
		
		initChild(devHelper);
	}
	
	abstract protected void initChild(DevHelper devHelper);
}
