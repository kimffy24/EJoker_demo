package pro.jiefzz.ejoker.test.javaGenericExpression.testH;

import java.lang.reflect.Field;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA1.C;

public class FuckH {

	public static void main(String[] args) {
		
		Class<?> clazz = C.class;
		
		Field[] declaredFields = clazz.getDeclaredFields();
		
		for(int i = 0; i<declaredFields.length; i++) {
			Field field = declaredFields[i];

			System.err.println(field.getGenericType().getClass());
			System.err.println(field.getGenericType());
			System.err.println(field.getType());
			System.err.println(field.getGenericType().getTypeName());
			System.err.println(field.getType().getTypeName());
			System.err.println();
		}
	}
}
