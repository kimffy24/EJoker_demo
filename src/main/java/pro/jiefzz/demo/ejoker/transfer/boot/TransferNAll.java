package pro.jiefzz.demo.ejoker.transfer.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.service.IScheduleService;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;

/**
 * 这是一个C端和Q端一起的demo<br />
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferNAll"
 * <br />* 远程调试添加到exec.args中 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n
 * @author kimffy
 *
 */
public class TransferNAll {

	private final static Logger logger = LoggerFactory.getLogger(TransferNAll.class);

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);

		scheduleService.startTask("xxx", () -> {
			logger.debug("Hello world!!!");
		}, 1000l, 1000l);
		
	}
}
