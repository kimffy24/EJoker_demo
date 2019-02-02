package pro.jiefzz.ejoker.test.javaGenericExpression.testA;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiefzz.ejoker.z.common.utils.GenericTypeUtil;

public class Main {

	public static void main(String[] args) throws Exception {
		
		ObjectMapper om = new ObjectMapper();
		
		Class<?>[] clazzs = new Class<?>[] {
			D0.class,
			D1.class,
			D2.class,
			Z.class
		};
		
		TypeVariable<Class<Object>>[] typeParameters = Object.class.getTypeParameters();
		System.err.println(typeParameters);
		System.err.println(typeParameters.length);
		
		for(Class<?> clazz : clazzs) {

			for(Class<?> currentClazz = clazz; !Object.class.equals(currentClazz); currentClazz = currentClazz.getSuperclass()) {
				
				System.err.print(currentClazz.getName());
				System.err.println(GenericTypeUtil.getClassDefinationGenericSignature(currentClazz));
				
				System.err.println("======= GenericType");
				Type genericSuperclass = currentClazz.getGenericSuperclass();
				if (genericSuperclass instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType )genericSuperclass;
					System.err.println(pt.getTypeName());
					Type[] actualTypeArguments = pt.getActualTypeArguments();
					for(Type varType:actualTypeArguments) {
						System.err.print(varType.getTypeName() + " ");
						System.err.println(varType.getClass());
					}
				}

				System.err.println("======= TypeParameters");
				TypeVariable<?>[] typeParameters2 = currentClazz.getSuperclass().getTypeParameters();
				for(TypeVariable<?> typeParameter : typeParameters2)
					System.err.println(typeParameter.getTypeName());

				System.err.println("==============");
					
			}
			
			System.err.println("============================");
			System.err.println();
			System.err.println();
		}
	}

}
