package pro.jiefzz.eden.treeSerializer;

import pro.jk.ejoker.EJoker;
import pro.jk.ejoker.EJoker.EJokerSingletonFactory;
import pro.jk.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jk.ejoker.common.context.dev2.impl.EjokerContextDev2Impl;
import pro.jk.ejoker.common.service.impl.JSONConverterUseJsonSmartImpl;

public class Main2 {

	public static void main(String[] args) {
		
		EJoker eJokerInstance = new EJokerSingletonFactory(EJoker.class).getInstance();
		IEJokerSimpleContext eJokerContext = eJokerInstance.getEJokerContext();

		{
			EjokerContextDev2Impl eJokerFullContext = (EjokerContextDev2Impl) eJokerContext;
			eJokerFullContext.scanPackage("pro.jiefzz.test");
		}

		StandardConverter x = new StandardConverter();
		
		JSONConverterUseJsonSmartImpl converter = new JSONConverterUseJsonSmartImpl();
		
		
		TestObject ts = new TestObject();
		
		String convert = converter.convert(ts);
		
		System.err.println(x.convert(ts));
		System.err.println(convert);
		TestObject revert = converter.revert(convert, TestObject.class);
		System.err.println(x.convert(revert));
		System.err.println(converter.convert(converter.revert(convert, TestObject.class)));
		
	}

}
