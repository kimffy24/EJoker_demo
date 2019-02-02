package pro.jiefzz.ejoker.test.treeSerializer;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.impl.EjokerContextDev2Impl;
import com.jiefzz.ejoker.z.common.service.impl.JSONConverterUseJsonSmartImpl;

public class Main2 {

	public static void main(String[] args) {
		
		EJoker eJokerInstance = EJoker.getInstance();
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
