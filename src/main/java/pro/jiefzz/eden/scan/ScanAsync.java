package pro.jiefzz.eden.scan;

import java.util.Set;
import java.util.TreeSet;

import pro.jiefzz.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jiefzz.ejoker.common.system.enhance.EachUtil;
import pro.jiefzz.ejoker.common.system.functional.IVoidFunction2;
import pro.jiefzz.ejoker.common.utils.genericity.GenericDefination;
import pro.jiefzz.ejoker.common.utils.genericity.GenericExpression;
import pro.jiefzz.ejoker.common.utils.genericity.GenericExpressionFactory;

public class ScanAsync {

	public static void main(String[] args) {

		final Set<String> concrete = new TreeSet<>();		
		final Set<String> upper = new TreeSet<>();
		
		IVoidFunction2<Class<?>, GenericDefination> t = (iclazz, ig) -> {
			if(!upper.contains(iclazz.getName() + ".*")) {
				upper.add(iclazz.getName() + ".*");
			}
		};

		EjokerContextDev2Impl c = new EjokerContextDev2Impl();
		c.registeScannerHook(clazz -> {
			GenericExpression middleStatementGenericExpression = GenericExpressionFactory.getMiddleStatementGenericExpression(clazz);
			GenericDefination genericDefination = middleStatementGenericExpression.genericDefination;
			concrete.add(clazz.getName() + ".*");
			
			for(GenericDefination g = genericDefination;
					null != g && !Object.class.equals(g.genericPrototypeClazz);
					g = g.getSuperDefination()
					) {
				
				if(upper.contains(g.genericPrototypeClazz + ".*"))
					break;
				
				upper.contains(g.genericPrototypeClazz + ".*");
				
				g.forEachInterfaceDefinations((iclazz, ig) -> {
					t.trigger(iclazz, ig);
					EachUtil.forEach(ig.getInterfaceDefinations(), (icl, igl) -> igl.forEachInterfaceDefinations(t));
				});
				
			}
		});
		c.scanPackage("com.jiefzz.ejoker");
		
		for(String s:concrete)
			System.err.println(s);
		
		System.err.println();

		for(String s:upper)
			System.err.println(s);
	}
}
