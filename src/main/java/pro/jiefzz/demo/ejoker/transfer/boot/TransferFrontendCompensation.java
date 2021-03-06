package pro.jiefzz.demo.ejoker.transfer.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jk.ejoker.commanding.ICommand;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.system.task.context.SystemAsyncHelper;
import pro.jk.ejoker.common.system.task.io.IOHelper;
import pro.jk.ejoker.queue.command.CommandService;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;

/**
 * 这是一个入口端的demo<br />
 * 主要用来创建账号和发送存款命令
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms2g -Xmx4g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferFrontend"
 * <br />* 远程调试添加到exec.args中 


 * 
 * @author kimffy
 * 测试saga过程中断后，通过主动补偿执行在中断处的命令，让过程继续。
 */
public class TransferFrontendCompensation {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferFrontendCompensation.class);

	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		
		CommandService commandService = eJokerContext.get(CommandService.class);
		SystemAsyncHelper systemAsyncHelper = eJokerFrameworkInitializer.getEJokerContext().get(SystemAsyncHelper.class);
		IOHelper ioHelper = eJokerFrameworkInitializer.getEJokerContext().get(IOHelper.class);
		
		List<ICommand> cmds = new ArrayList<>();
		
		String[] tids = new String[] {
				
		                  };
		
		for(String s : tids)
			cmds.add(new ConfirmDepositPreparationCommand(s));
		
		CountDownLatch cdl = new CountDownLatch(cmds.size());
		long t = System.currentTimeMillis();
		for(int j=0; j<cmds.size(); j++) {
				ICommand startDepositTransactionCmd = cmds.get(j);
				systemAsyncHelper.submit(() -> {
					ioHelper.tryAsyncAction2(
							"TestSend_",
							() -> commandService.sendAsync(startDepositTransactionCmd),
							r -> cdl.countDown(),
							() -> "",
							e -> e.printStackTrace(),
							true);
				});
		}
		cdl.await();
		String msg = String.format("batch start at: %d, time use: %d ms", t, System.currentTimeMillis() - t);
		logger.error(msg);
		System.err.println(msg);
		
		eJokerFrameworkInitializer.discard();
	}
}
