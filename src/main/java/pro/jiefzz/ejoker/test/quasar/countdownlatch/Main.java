package pro.jiefzz.ejoker.test.quasar.countdownlatch;

import java.util.concurrent.TimeUnit;

import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

import co.paralleluniverse.strands.concurrent.CountDownLatch;

public class Main {
	
	public static CountDownLatch cdl = new CountDownLatch(1);
	
	private static Thread thread = new Thread(() -> {
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("child thread: ok~");
	});
	
	static {
		thread.start();
		System.err.println("child thread started.");
	}

	public static void main(String[] args) {
		
		SleepWrapper.sleep(TimeUnit.SECONDS, 5l);

		System.err.println("try release cdl.");
		cdl.countDown();
		System.err.println("cdl released.");

		SleepWrapper.sleep(TimeUnit.SECONDS, 1l);
	}

}
