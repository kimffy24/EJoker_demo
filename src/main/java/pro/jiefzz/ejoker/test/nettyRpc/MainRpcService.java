package pro.jiefzz.ejoker.test.nettyRpc;

import java.util.concurrent.locks.LockSupport;

import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.rpc.IRPCService;

public class MainRpcService {

	public static void main(String[] args) {
		
		EJokerBootstrap ejb = new EJokerBootstrap();
		
		IEJokerSimpleContext eJokerContext = ejb.getEJokerContext();
		
		IRPCService rpcService = eJokerContext.get(IRPCService.class);
		
		rpcService.export(System.out::println, 33433, true);
		
		for(int x=1; x<100; x++)
			rpcService.remoteInvoke("Hello world!!! " + x, "127.0.0.1", 33433);
		
		LockSupport.park();
		((IEjokerContextDev2 )eJokerContext).discard();
	}
}
