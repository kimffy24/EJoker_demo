package pro.jiefzz.eden.nettyRpc;

import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.context.dev2.IEjokerContextDev2;
import pro.jk.ejoker.common.service.rpc.IRPCService;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;

public class MainRpcService {
	
	private final static Logger logger = LoggerFactory.getLogger(MainRpcService.class);

	public static void main(String[] args) {
		
		EJokerBootstrap ejb = new EJokerBootstrap("pro.jk.ejoker_support.defaultMemoryImpl", "pro.jiefzz.eden.aa", "pro.jiefzz.eden.nettyRpc");
		
		IEJokerSimpleContext eJokerContext = ejb.getEJokerContext();
		
		IRPCService rpcService = eJokerContext.get(IRPCService.class);
		
		rpcService.export(m -> {
			logger.info("content: {}, t: {}", m, Thread.currentThread().getName());
		}, 33433, true);
		
		for(int x=1; x<100; x++)
			rpcService.remoteInvoke("Hello world!!! " + x, "127.0.0.1", 33433);
		
		LockSupport.park();
		((IEjokerContextDev2 )eJokerContext).discard();
	}
}
