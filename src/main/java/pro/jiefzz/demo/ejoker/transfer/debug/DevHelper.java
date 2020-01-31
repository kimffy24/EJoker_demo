package pro.jiefzz.demo.ejoker.transfer.debug;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import pro.jiefzz.ejoker.common.context.dev2.EjokerRootDefinationStore;
import pro.jiefzz.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jiefzz.ejoker.common.system.enhance.EachUtil;
import pro.jiefzz.ejoker.common.system.enhance.MapUtil;

public final class DevHelper {
	
	private final static Map<Object, DevHelper> HelperInstanceStore = new ConcurrentHashMap<>();
	
	public final static DevHelper of(Object context) {
		return MapUtil.getOrAdd(HelperInstanceStore, context, () -> new DevHelper(context));
	}
	
	protected EjokerRootDefinationStore defaultRootDefinationStore = null;
	
	protected Set<Class<?>> hasBeenAnalyzeClass = null;
	
	private DevHelper(Object context) {
		if(!(context instanceof EjokerContextDev2Impl)) {
			throw new RuntimeException("parameter #1 is not a ejoker context");
		}
		connect((EjokerContextDev2Impl )context, "defaultRootDefinationStore");
		connect(defaultRootDefinationStore, "hasBeenAnalyzeClass");
		connectField(this);
	}
	
	private void connect(Object obj, String sourceName) {
		connect(obj, sourceName, this);
	};
	
	/**
	 * 从给定对象里找到名为sourceName的属性 并绑定到当前对象的同名属性中
	 * @param obj
	 * @param sourceName
	 */
	public final void connect(Object obj, String sourceName, Object dist) {
		connect(obj, sourceName, dist, sourceName);
	};

	/**
	 * 从给定对象里找到名为sourceName的属性 并绑定到当前对象的名为targetName的属性中<br /><br />
	 * 找不到或null会抛出 RuntimeException
	 * @param obj
	 * @param sourceName
	 * @param targetName
	 */
	public final void connect(Object obj, String sourceName, Object dist, String targetName) {
		Field f = getDF(obj.getClass(), sourceName);
		Object target;
		try {
			target = f.get(obj);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		if(null == target)
			throw new RuntimeException(String.format("Null fetch for field[name=%s]!!! sourceName: %s, from: %s", targetName, sourceName, obj.getClass().getName()));

		try {
			getDF(dist.getClass(), targetName).set(dist, target);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e1) {
			throw new RuntimeException(e1);
		}
	}

	/**
	 * 填充本对象里的形如 dF_{class_simple_name}_{field_name} 的属性反射对象，
	 * 目标为 class_simple_name.field_name ， 
	 * 找不对 class_simple_name 类 或 找不到 field_name 的属性，抛出 RuntimeException ，
	 * 如果有多个类的simple_name 匹配 class_simple_name ， 抛出 RuntimeException
	 */
	public final void connectField(Object target) {
		Set<String> fieldsBefore = new HashSet<>();
		for(Class<?> clazz = target.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
			Field[] declaredFields = clazz.getDeclaredFields();
			EachUtil.forEach(declaredFields, f -> {
				if(!f.getName().startsWith("dF_"))
					return;
				if(fieldsBefore.contains(f.getName()))
					return;
				String[] sg = f.getName().split("_");
				if(null == sg || sg.length != 3) {
					throw new RuntimeException(String.format("Invalid dF_ field name!!! [class=%s, field=%s]", target.getClass().getName(), f.getName()));
				}
				List<Class<?>> matchClazzs = hasBeenAnalyzeClass.stream().filter(c -> c.getSimpleName().equals(sg[1])).collect(Collectors.toList());
				if(1 != matchClazzs.size()) {
					throw new RuntimeException(String.format("%s faild match!!! [class=%s, field=%s] find result: %s", sg[1], this.getClass().getName(), f.getName(), null == matchClazzs ? "null" : matchClazzs.toString()));
				}
				f.setAccessible(true);
				Class<?> matchClazz = matchClazzs.get(0);
				try {
					f.set(target, getDF(matchClazz, sg[2]));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				fieldsBefore.add(f.getName());
			});
		}
	}
	
	/**
	 * 從對象中獲取某個field的值，並指名類型。
	 * @param <T>
	 * @param f
	 * @param from
	 * @param typeRef
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T fieldValue(Field f, Object from, Class<T> typeRef) {
		Object target;
		try {
			target = f.get(from);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return (T )target;
	}
	
	/**
	 * 从类里获取名为fieldName的属性的反射对象 <br /><br />
	 * 如果获取不到，会抛出 RuntimeException
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public final static Field getDF(Class<?> clazz, String fieldName) {
		Field f = null;
		try {
			for(Class<?> c = clazz;
					null == f
					&& !RuntimeException.class.equals(c)
						&& !Exception.class.equals(c) 
						&& !Object.class.equals(c) ;
					c = c.getSuperclass()) {
				try {
					f = c.getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
					continue;
				}
			}
			if(null == f)
				throw new RuntimeException(String.format("Found no such field[name=%s] from Class<%s>", fieldName, clazz.getName()));
			f.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return f;
	}
}
