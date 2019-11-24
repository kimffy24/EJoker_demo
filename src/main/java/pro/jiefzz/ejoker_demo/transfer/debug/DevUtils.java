package pro.jiefzz.ejoker_demo.transfer.debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.common.system.enhance.ForEachUtil;

public final class DevUtils {
	
	private final static String[] probeTargets = new String[] {
			
			"pro.jiefzz.ejoker_demo.transfer.eventHandlers.ConsoleLogger",
			
	};
	
	public static long moniterQ(IEJokerSimpleContext eJokerContext) {

		ForEachUtil.processForEach(mStore, (c ,m) -> {
			Object object = eJokerContext.get(c);
			if(null == object)
				return;
			try {
				m.invoke(c);
			} catch (IllegalAccessException|IllegalArgumentException e) {
				;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		});
		
		return System.currentTimeMillis();
	}

	private final static String mName = "probe";
	
	private final static Map<Class<?>, Method> mStore = new HashMap<>();
	
	static {
		
		for(String t : probeTargets) {
			Method m = null;
			Class<?> cl = null;
			try {
				cl = Class.forName(t);
			} catch (ClassNotFoundException e) {
				continue;
			}
			try {
				for(Class<?> c = cl;
						!RuntimeException.class.equals(c)
							&& !Exception.class.equals(c) 
							&& !Object.class.equals(c) ;
						c = c.getSuperclass())
					try {
						m = c.getDeclaredMethod(mName);
					} catch (NoSuchMethodException e) {
						continue;
					}
				if(null != m) {
					m.setAccessible(true);
					mStore.put(cl, m);
				}
			} catch (SecurityException e) {
				;
			}
		}
		
	}
}
