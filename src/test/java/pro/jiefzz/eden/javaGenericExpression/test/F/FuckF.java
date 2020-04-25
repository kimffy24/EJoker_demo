package pro.jiefzz.eden.javaGenericExpression.test.F;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class FuckF {

	public static void main(String[] args) {
		
		System.err.println(Serializable.class.getSuperclass());
		System.err.println(Serializable.class.getGenericSuperclass());
		System.err.println(Serializable.class.getGenericSuperclass() instanceof ParameterizedType);

		System.err.println(Serializable.class.getInterfaces());
		System.err.println(Serializable.class.getInterfaces().length);
		System.err.println(Serializable.class.getGenericInterfaces());
		System.err.println(Serializable.class.getGenericInterfaces().length);
		
		System.err.println(Serializable.class.getGenericSuperclass() instanceof ParameterizedType);
	}

}
