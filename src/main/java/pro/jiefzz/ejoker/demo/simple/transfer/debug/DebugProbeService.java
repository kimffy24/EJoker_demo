package pro.jiefzz.ejoker.demo.simple.transfer.debug;

import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;

@EService
public class DebugProbeService {

	@Dependence
	IScheduleService scheduleService;
	
	private void probe() {
		
	}
	
}
