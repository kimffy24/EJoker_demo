package pro.jiefzz.eden.nettyRpc;

import java.util.concurrent.TimeUnit;

import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.z.context.dev2.IEjokerContextDev2;
import pro.jiefzz.ejoker.z.rpc.IRPCService;
import pro.jiefzz.ejoker.z.system.wrapper.SleepWrapper;
import pro.jiefzz.ejoker_demo.transfer.boot.AbstractEJokerBootstrap;
import pro.jiefzz.ejoker_demo.transfer.boot.TransferPrepare;

public class TestRpcClient {

	public static void main(String[] args) {
		
		AbstractEJokerBootstrap ejb = TransferPrepare.prepare(new EJokerBootstrap());
		
		IEJokerSimpleContext eJokerContext = ejb.getEJokerContext();
		
		IRPCService rpcService = eJokerContext.get(IRPCService.class);
		
		for(int x=0; x<2; x++) {
			String data = String.format("{\"%s\": \"%s\", \"%s\": %d}", "key", "test", "index", x);
			rpcService.remoteInvoke(data, "192.168.199.254", 65056);
			SleepWrapper.sleep(TimeUnit.MILLISECONDS, 30000l);
		}
		
		SleepWrapper.sleep(TimeUnit.MILLISECONDS, 100l);
		
		((IEjokerContextDev2 )eJokerContext).discard();
	}
}