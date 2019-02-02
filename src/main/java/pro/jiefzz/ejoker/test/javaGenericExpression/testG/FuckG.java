package pro.jiefzz.ejoker.test.javaGenericExpression.testG;

import java.lang.reflect.Type;

public class FuckG {

	public static void main(String[] args) {
		
		Class<? extends String[]> class1 = args.getClass();
		
		System.err.println(class1.isArray());
		System.err.println(class1.getComponentType());
		System.err.println(String.class.equals(class1.getComponentType()));
		
		System.err.println(class1 instanceof Type);
	}

}
