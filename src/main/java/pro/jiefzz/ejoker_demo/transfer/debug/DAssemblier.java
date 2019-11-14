package pro.jiefzz.ejoker_demo.transfer.debug;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EInitialize;
import pro.jiefzz.ejoker.common.context.dev2.EjokerRootDefinationStore;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.common.system.enhance.ForEachUtil;
import pro.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;

public abstract class DAssemblier extends AbstractMessageHandler {

	@Dependence
	IEJokerSimpleContext eJokerContext;
	
	private boolean use = false;
	
	private int initx = 0;
	
	public boolean isUse() {
		
		if(!use && 5 > initx) {
			if(tryAssembly())
				use = true;
			else
				initx++;
		}
		
		return use;
	}
	
	protected boolean tryAssembly() {
		return false;
	}
	
	protected boolean isActive() {
		return false;
	}
	
	@EInitialize(priority = 10)
	private void initFather() {

		
		connect(eJokerContext, "defaultRootDefinationStore");
		connect(defaultRootDefinationStore, "hasBeenAnalyzeClass");
		connectField();
		
		// ==============
	}
	
	protected EjokerRootDefinationStore defaultRootDefinationStore = null;
	
	protected Set<Class<?>> hasBeenAnalyzeClass = null;
	
	public <T> T fieldValue(Field f, Object from, Class<T> typeRef) {
		Object target;
		try {
			target = f.get(from);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return (T )target;
	}

	/**
	 * 从对象里获取名为fieldName的属性的反射对象
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static final Field getDF(Object obj, String fieldName) {
		return getDF(obj.getClass(), fieldName);
	}
	
	/**
	 * 从类里获取名为fieldName的属性的反射对象 <br /><br />
	 * 如果获取不到，会抛出 RuntimeException
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static final Field getDF(Class<?> clazz, String fieldName) {
		Field f = null;
		try {
			for(Class<?> c = clazz;
					!RuntimeException.class.equals(c)
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
	
	/**
	 * 从给定对象里找到名为sourceName的属性 并绑定到当前对象的同名属性中
	 * @param obj
	 * @param sourceName
	 */
	protected void connect(Object obj, String sourceName) {
		connect(obj, sourceName, sourceName);
	};
	
	/**
	 * 从给定对象里找到名为sourceName的属性 并绑定到当前对象的名为targetName的属性中<br /><br />
	 * 找不到或null会抛出 RuntimeException
	 * @param obj
	 * @param sourceName
	 * @param targetName
	 */
	protected void connect(Object obj, String sourceName, String targetName) {
		Field f = getDF(obj, sourceName);
		Object target;
		try {
			target = f.get(obj);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		if(null == target)
			throw new RuntimeException(String.format("Null fetch for field[name=%s]!!! sourceName: %s, from: %s", targetName, sourceName, obj.getClass().getName()));

		try {
			getDF(this, targetName).set(this, target);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e1) {
			throw new RuntimeException(e1);
		}
	};
	
	/**
	 * 填充本对象里的形如 dF_{class_simple_name}_{field_name} 的属性反射对象，
	 * 目标为 class_simple_name.field_name ， 
	 * 找不对 class_simple_name 类 或 找不到 field_name 的属性，抛出 RuntimeException ，
	 * 如果有多个类的simple_name 匹配 class_simple_name ， 抛出 RuntimeException
	 */
	protected void connectField() {
		for(Class<?> clazz = this.getClass(); !clazz.equals(AbstractMessageHandler.class); clazz = clazz.getSuperclass()) {
			Field[] declaredFields = clazz.getDeclaredFields();
			ForEachUtil.processForEach(declaredFields, f -> {
				if(!f.getName().startsWith("dF_"))
					return;
				String[] sg = f.getName().split("_");
				if(null == sg || sg.length != 3) {
					throw new RuntimeException(String.format("Invalid dF_ field name!!! [class=%s, field=%s]", this.getClass().getName(), f.getName()));
				}
				List<Class<?>> matchClazzs = hasBeenAnalyzeClass.stream().filter(c -> c.getSimpleName().equals(sg[1])).collect(Collectors.toList());
				if(1 != matchClazzs.size()) {
					throw new RuntimeException(String.format("%s faild match!!! [class=%s, field=%s] find result: %s", sg[1], this.getClass().getName(), f.getName(), null == matchClazzs ? "null" : matchClazzs.toString()));
				}
				
				Class<?> matchClazz = matchClazzs.get(0);
				try {
					f.set(this, getDF(matchClazz, sg[2]));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			});
		}
	}
	
}
