package pro.jiefzz.ejoker.test.lajijava.stream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.jiefzz.ejoker.z.common.system.helper.MapHelper;

public class XStream {

	public static void main(String[] args) {
		
		Map<String, Long> versionDict = new ConcurrentHashMap<>();
		
		versionDict.put("a", 100l);
		versionDict.put("b", 101l);
		versionDict.put("c", 102l);
		versionDict.put("d", 100l);
		versionDict.put("e", 100l);
		versionDict.put("f", 101l);
		versionDict.put("g", 102l);
		versionDict.put("h", 100l);
		versionDict.put("j", 100l);
		versionDict.put("k", 101l);
		versionDict.put("l", 102l);
		versionDict.put("z", 100l);
		
		Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
		versionDict.entrySet().parallelStream().map(e -> {
			return e.getValue();
		}).mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, AtomicLong::new).incrementAndGet()).sum();
		
		
	}

}
