package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.locks.LockSupport;

/**
 * 清理掉测试时遗留在队列中的信息
 * @author kimffy
 *
 */
public class TransferTestLajiMessageConsumer {

	public static void main(String[] args) throws Exception {
		new Thread(() -> {
			try {
				TransferTestLajiEventConsumer.main(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				TransferTestLajiCommandConsumer.main(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		LockSupport.park();
	}

}
