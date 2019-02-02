package pro.jiefzz.ejoker.test.nettyRpc;

import java.util.concurrent.locks.LockSupport;

import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.rpc.IRPCService;

public class TestRpcClient {

	public static void main(String[] args) {
		
		EJokerBootstrap ejb = new EJokerBootstrap();
		
		IEJokerSimpleContext eJokerContext = ejb.getEJokerContext();
		
		IRPCService rpcService = eJokerContext.get(IRPCService.class);
		
		for(int x=1; x<100; x++)
			rpcService.remoteInvoke(x + "Hello world!!! \n", "192.168.199.52", 65056);
		
		LockSupport.park();
		((IEjokerContextDev2 )eJokerContext).discard();
	}
}
