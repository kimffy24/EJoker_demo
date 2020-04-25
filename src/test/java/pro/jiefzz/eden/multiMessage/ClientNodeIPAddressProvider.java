package pro.jiefzz.eden.multiMessage;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.service.rpc.IClientNodeIPAddressProvider;

@EService
public class ClientNodeIPAddressProvider implements IClientNodeIPAddressProvider {

	@Override
	public String getClientNodeIPAddress() {
		return "192.168.199.123";
	}

}
