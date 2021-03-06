package pro.jiefzz.eden.scan;

import java.util.Set;
import java.util.TreeSet;

import pro.jk.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jk.ejoker.common.system.enhance.EachUtilx;
import pro.jk.ejoker.common.system.functional.IVoidFunction2;
import pro.jk.ejoker.common.utils.genericity.GenericDefinition;
import pro.jk.ejoker.common.utils.genericity.GenericExpression;
import pro.jk.ejoker.common.utils.genericity.GenericExpressionFactory;

public class ScanAsync {

	public static void main(String[] args) {

		final Set<String> concrete = new TreeSet<>();		
		final Set<String> upper = new TreeSet<>();
		
		IVoidFunction2<Class<?>, GenericDefinition> t = (iclazz, ig) -> {
			if(!upper.contains(iclazz.getName() + ".*")) {
				upper.add(iclazz.getName() + ".*");
			}
		};

		EjokerContextDev2Impl c = new EjokerContextDev2Impl();
		((EjokerContextDev2Impl )c).getEJokerRootDefinationStore().registeScannerHook(clazz -> {
			GenericExpression middleStatementGenericExpression = GenericExpressionFactory.getMiddleStatementGenericExpression(clazz);
			GenericDefinition genericDefination = middleStatementGenericExpression.genericDefination;
			concrete.add(clazz.getName() + ".*");
			
			for(GenericDefinition g = genericDefination;
					null != g && !Object.class.equals(g.genericPrototypeClazz);
					g = g.getSuperDefination()
					) {
				
				if(upper.contains(g.genericPrototypeClazz + ".*"))
					break;
				
				upper.contains(g.genericPrototypeClazz + ".*");
				
				g.forEachInterfaceDefinations((iclazz, ig) -> {
					t.trigger(iclazz, ig);
					EachUtilx.forEach(ig.getInterfaceDefinations(), (icl, igl) -> igl.forEachInterfaceDefinations(t));
				});
				
			}
		});
		((EjokerContextDev2Impl )c).getEJokerRootDefinationStore().scanPackage("com.jiefzz.ejoker");
		
		for(String s:concrete)
			System.err.println(s);
		
		System.err.println();

		for(String s:upper)
			System.err.println(s);
	}
}
