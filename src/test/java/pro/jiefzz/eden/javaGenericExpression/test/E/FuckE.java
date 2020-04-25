package pro.jiefzz.eden.javaGenericExpression.test.E;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import pro.jiefzz.eden.javaGenericExpression.test.A.C;
import pro.jiefzz.eden.javaGenericExpression.test.A.D0;

public class FuckE {

	public static void main(String[] args) {
		new FuckE().start();
	}

	Map<Integer, ? extends C<?, ?>> pMapSuper = null;
	
	public void start() {
		
		Map<Integer, C<?, ?>> pMap = new HashMap<>();
		
		pMap.put(1, new D0<Integer>());
		pMap.put(2, new D0<Double>());
		
		
		pMapSuper = pMap;
		
		C<?, ?> c = pMapSuper.get(new Integer(1));
		
		System.err.println(c);
		System.err.println(c.getClass());
		TypeVariable<?>[] typeParameters = pMap.getClass().getTypeParameters();
		for(TypeVariable<?> type : typeParameters) {
			System.err.println(type.getTypeName());
		}
		
		Field[] declaredFields = FuckE.class.getDeclaredFields();
		for(Field field : declaredFields) {
			Type genericType = field.getGenericType();
		}
	}

}
