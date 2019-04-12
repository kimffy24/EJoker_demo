package pro.jiefzz.ejoker.test.nettyRpc;

import java.util.concurrent.TimeUnit;

import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.rpc.IRPCService;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

import pro.jiefzz.ejoker.demo.simple.transfer.TransferPrepare;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

public class TestRpcClient {

	public static void main(String[] args) {
		
		EJokerBootstrap ejb = TransferPrepare.prepare(new EJokerBootstrap());
		
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
