package pro.jiefzz.eden.aa;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.service.rpc.IClientNodeIPAddressProvider;

@EService
public class TestClientIPProvider implements IClientNodeIPAddressProvider {

	@Override
	public String getClientNodeIPAddress() {
		return null;
	}

}
