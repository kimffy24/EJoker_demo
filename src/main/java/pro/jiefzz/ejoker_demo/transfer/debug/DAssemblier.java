package pro.jiefzz.ejoker_demo.transfer.debug;

import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EInitialize;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;

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
