package pro.jiefzz.ejoker.test.lajijava.treeMapTest;

import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapTest {

	public static void main(String[] args) {

		TreeMap<Long, String> treeMap = new TreeMap<>(
				new Comparator<Long>() {
					public int compare(Long k1,Long k2) {
						return k1-k2>0?1:-1;//将str1和str2调换位置是倒序排序	
					}			
				});

		treeMap.put(56l, "16");
		
		treeMap.put(16l, "16");

		treeMap.put(System.currentTimeMillis(), "16");

		treeMap.put(-1l, "16");
		
		System.err.println(treeMap);

		System.err.println(treeMap.lastKey());

		System.err.println(treeMap.firstKey());
		
	}

}
