package pro.jiefzz.ejoker_demo.transfer.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.z.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.z.context.annotation.context.EInitialize;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.z.service.IScheduleService;
import pro.jiefzz.ejoker.z.system.functional.IVoidFunction;

@EService
public class Console {
	
	private final static Logger logger = LoggerFactory.getLogger(Console.class);
	
	@Dependence
	IEJokerSimpleContext eJokerContext;

	@Dependence
	DebugHelperEJoker debugHelperEJoker;
	
	@Dependence
	DebugHelperJavaQueue debugHelperJavaQueue;

	@Dependence
	DebugHelperMQ debugHelperMQ;
	
	@Dependence
	IScheduleService scheduleService;
	
	private IVoidFunction[] probeChain = null;

	private IVoidFunction[] probeAlwaysChain = null;
	
	@EInitialize
	private void init() {
		addProbeAlwaysChain(debugHelperJavaQueue::probeStatictics);
		scheduleService.startTask("Console_probe", this::probe, 5000l, 5000l);
	}
	
	public synchronized void addProbeChain(IVoidFunction vf) {
		if(null == probeChain) {
			probeChain = new IVoidFunction[] {vf};
		} else {
			IVoidFunction[] probeChainx = new IVoidFunction[probeChain.length+1];
			System.arraycopy(probeChain, 0, probeChainx, 0, probeChain.length);
			probeChainx[probeChain.length] = vf;
			this.probeChain = probeChainx;
		}
	}
	
	public synchronized void addProbeAlwaysChain(IVoidFunction vf) {
		if(null == probeAlwaysChain) {
			probeAlwaysChain = new IVoidFunction[] {vf};
		} else {
			IVoidFunction[] probeChainx = new IVoidFunction[probeAlwaysChain.length+1];
			System.arraycopy(probeAlwaysChain, 0, probeChainx, 0, probeAlwaysChain.length);
			probeChainx[probeAlwaysChain.length] = vf;
			this.probeAlwaysChain = probeChainx;
		}
	}
	
	public void sepOnce() {
		debugHelperJavaQueue.sepOnce();
	}
	
	public void setStart() {
		debugHelperJavaQueue.turnOnStatictics();
	}
	
	private void probe() {

		// ======== 每次调度都运行的
		if(null != probeAlwaysChain)
			for(IVoidFunction vfx : probeAlwaysChain) {
				vfx.trigger();
			}
		
		// ======== isActive==false 后才进行的 （latest 30 秒没变视为跑完）
		if(!debugHelperEJoker.isActive() && !debugHelperJavaQueue.isActive()) {

			debugHelperEJoker.probe();
			debugHelperJavaQueue.probe();
			debugHelperMQ.probe();
			
			// 输出transfer的设置的统计数据
			DevUtils.moniterQ(eJokerContext);

			if(null != probeChain)
				for(IVoidFunction vfx : probeChain) {
					vfx.trigger();
				}
			
		}
		
		logger.error("\t");
	}
}
