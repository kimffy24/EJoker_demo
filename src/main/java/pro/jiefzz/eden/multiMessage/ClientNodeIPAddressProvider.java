package pro.jiefzz.eden.multiMessage;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.service.rpc.IClientNodeIPAddressProvider;

@EService
public class ClientNodeIPAddressProvider implements IClientNodeIPAddressProvider {

	@Override
	public String getClientNodeIPAddress() {
		return "192.168.199.123";
	}

}
