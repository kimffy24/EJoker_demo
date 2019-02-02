package pro.jiefzz.ejoker.test.javaGenericExpression.testk;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FuckK {

	public static void main(String[] args) {
		
		ExecutorService poolInstance = Executors.newFixedThreadPool(8);

		CountDownLatch cdl = new CountDownLatch(1);
		
		for(int i=0; i<8; i++)
			poolInstance.execute(() -> {
				try {
					cdl.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("a"); 
			});
		poolInstance.execute(() -> {
			System.err.println("123");
		});
		
		new Thread(() ->  {
			try {
				TimeUnit.SECONDS.sleep(10l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cdl.countDown();
		}).start();
		
		poolInstance.shutdown();
		
	}
}
