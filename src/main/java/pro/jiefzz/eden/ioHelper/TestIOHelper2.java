package pro.jiefzz.eden.ioHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import pro.jiefzz.ejoker.common.system.task.io.IOHelper;

public class TestIOHelper2 {

	public static void main(String[] args) {

		new TestIOHelper2().start();
		LockSupport.park();
	}

	IOHelper ioHelper = new IOHelper();

	public final List<String> delegateRequests = new ArrayList<>();

	public void start() {

		int a=5;
		
		if(5 == a--) {
			System.out.println("a = " + a);
			System.out.println("5 == a--");
		}
		
		a=5;
		
		if(5 == --a) {
			System.out.println("a = " + a);
			System.out.println("5 == --a");
		}
		
		delegateRequests.add("a");
		delegateRequests.add("b");
		delegateRequests.add("c");
		delegateRequests.add("d");

		System.out.println(delegateRequests.get(1));
		System.out.println(delegateRequests.get(2));
		System.out.println(delegateRequests.get(3));
		
		
		delegateRequests.remove(1);
		
		System.out.println(delegateRequests.get(1));
		System.out.println(delegateRequests.get(2));
		System.out.println(delegateRequests.get(3));

	}
}
