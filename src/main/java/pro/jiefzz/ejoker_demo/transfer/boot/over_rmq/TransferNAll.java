package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.z.schedule.IScheduleService;
import pro.jiefzz.ejoker_demo.transfer.boot.AbstractEJokerBootstrap;
import pro.jiefzz.ejoker_demo.transfer.boot.TransferPrepare;

/**
 * 这是一个C端和Q端一起的demo<br />
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferNAll"
 * <br />* 远程调试添加到exec.args中 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n
 * @author kimffy
 *
 */
public class TransferNAll {

	private final static Logger logger = LoggerFactory.getLogger(TransferNAll.class);
	
	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
	public static void start(AbstractEJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initAll();
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);

		scheduleService.startTask("xxx", () -> {
			logger.debug("Hello world!!!");
		}, 1000l, 1000l);
		
	}
}
