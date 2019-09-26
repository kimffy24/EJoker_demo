package pro.jiefzz.eden.nettyRpc;

import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.rpc.IClientNodeIPAddressProvider;

@EService
public class ClientNodeIPAddressProvider implements IClientNodeIPAddressProvider {

	@Override
	public String getClientNodeIPAddress() {
		return "192.168.199.123";
	}

}
