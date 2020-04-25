package pro.jiefzz.eden.chm;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

import pro.jk.ejoker.common.system.functional.IFunction2;

public class ConcurrentHashMapTest {

	public static void main(String[] args) {
		
		Map<String, String> m = new ConcurrentHashMap<>();
		
		for(int i=0; i<10000; i++)
			m.put("" +i, ""+(i+20000));
		
		new Thread(()-> {
			String csb = "";
			while(true) {
				{
					Set<Entry<String,String>> entrySet = m.entrySet();
					for(Entry<String,String> ety : entrySet) {
						System.out.print("" + ety.getKey() + "=" + ety.getValue() + ", ");
					}
					System.out.println();
				}
				System.out.println(m);
				System.out.println(m.size());
				String sb = "";
				for(int j = 0; j <ds.length; j++) {
					sb += "" + ds[j] +", ";
				}
				System.out.println(sb);
				if(!csb.equals(sb)) {
					csb = sb;
				} else {
					System.exit(0);
				}
			}
		}).start();
		
		new Thread(()-> eff(0, m, (k, v) -> k.startsWith("3"))).start();
		new Thread(()-> eff(1, m, (k, v) -> k.startsWith("21"))).start();
		new Thread(()-> eff(2, m, (k, v) -> k.startsWith("31"))).start();
		new Thread(()-> eff(3, m, (k, v) -> k.startsWith("2"))).start();
		new Thread(()-> m.remove("8866")).start();
		new Thread(()-> eff(4, m, (k, v) -> v.endsWith("7"))).start();
		new Thread(()-> eff(5, m, (k, v) -> v.endsWith("399"))).start();
		new Thread(()-> eff(6, m, (k, v) -> v.endsWith("9"))).start();
		new Thread(()-> eff(7, m, (k, v) -> v.endsWith("599"))).start();
		
		
		LockSupport.park();
	}
	
	static int[] ds = new int[] {0, 0, 0, 0, 0, 0, 0, 0};

	public static void eff(int x, Map<String, String> m, IFunction2<Boolean, String, String> prediction) {
		Iterator<Entry<String, String>> iterator = m.entrySet().iterator();
		while(iterator.hasNext()) {
//			DiscardWrapper.sleepInterruptable(1l);
			Entry<String, String> curr = iterator.next();
			if(prediction.trigger(curr.getKey(), curr.getValue())) {
				iterator.remove();
				ds[x] = ds[x]+1;
			}
		}
	}
}
