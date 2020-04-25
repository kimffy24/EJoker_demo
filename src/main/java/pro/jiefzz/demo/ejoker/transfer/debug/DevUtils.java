package pro.jiefzz.demo.ejoker.transfer.debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jk.ejoker.common.system.enhance.MapUtilx;

public final class DevUtils {
	
	private final static String mName = "probe";
	
	private final static Map<Object, DevUtils> devUtilsStore = new ConcurrentHashMap<>();
	
	private final static DevUtils getCurrentDevUtils(Object eJokerContext) {
		return MapUtilx.getOrAdd(devUtilsStore, eJokerContext, () -> new DevUtils(eJokerContext));
	}

	private final Map<String, Object> instanceMap = null;

	private final Map<String, Object> instanceGenericTypeMap = null;

	private final Map<Object, Method> mStore = new HashMap<>();
	
	private DevUtils(Object eJokerContext) {
		if(!(eJokerContext instanceof EjokerContextDev2Impl)) {
			throw new RuntimeException("parameter #1 is not a ejoker context");
		}
		DevHelper devHelper = DevHelper.of(eJokerContext);
		devHelper.connect(eJokerContext, "instanceMap", this);
		devHelper.connect(eJokerContext, "instanceGenericTypeMap", this);
		
		instanceMap.forEach((k, v) -> {
			Method m = null;
			try {
				for(Class<?> c = v.getClass();
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
					mStore.put(v, m);
				}
			} catch (SecurityException e) {
				;
			}
		});

		instanceGenericTypeMap.forEach((k, v) -> {
			Method m = null;
			try {
				for(Class<?> c = v.getClass();
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
					mStore.put(v, m);
				}
			} catch (SecurityException e) {
				;
			}
		});
	}
	
	/**
	 * 从eJokerContext中找出probeTargets中定义的实例，并为他们执行名为probe方法
	 * @param eJokerContext
	 * @return
	 */
	public static long moniterQ(IEJokerSimpleContext eJokerContext) {
		long execTMillis = System.currentTimeMillis();
		DevUtils currentDevUtils = getCurrentDevUtils(eJokerContext);
		currentDevUtils.mStore.forEach((c ,m) -> {
			if(null == c)
				return;
			try {
				m.invoke(c);
			} catch (IllegalAccessException|IllegalArgumentException e) {
				;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		});
		
		return execTMillis;
	}

}
