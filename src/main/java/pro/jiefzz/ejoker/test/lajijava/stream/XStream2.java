package pro.jiefzz.ejoker.test.lajijava.stream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XStream2 {

	public static void main(String[] args) {
		
		Map<Long, String> aheadCompletion = new ConcurrentHashMap<>();

		aheadCompletion.put(1l, "");
		aheadCompletion.put(3l, "");
		aheadCompletion.put(4l, "");
		aheadCompletion.put(5l, "");
		aheadCompletion.put(7l, "");
		aheadCompletion.put(8l, "");		
		aheadCompletion.put(9l, "");
		
		aheadCompletion.keySet().stream().sorted().reduce(0l, (a, b) -> {
			if(0l < a) {
				if(a + 1 == b) {
					;
				} else {
					System.err.println(String.format("前值: %d, 后值: %d", a, b));
				}
			}
			return b;
		});
		
	}

}
