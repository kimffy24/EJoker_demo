package pro.jiefzz.ejoker.test.javaGenericExpression.testD;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.D0;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.D1;

public class Fuck {
	
	public void a(String a, Map<String, Map<String, ?>> pMap, List<? extends Number> pList) {
		
	}
	
	public void b(int x, Map<Integer, Map> pMapb1, Map<Integer, Map<String, D0<? extends Number>[]>> pMapb2) {
		
	}
	
	public void c(D0<? extends Number>[] x1, D0<D1<String, Number>> x2) {
		
	}

	public static void main(String[] args) {

		for(
				Class<?> clayy = TypeVariable.class;
				null != clayy && !Object.class.equals(clayy);
				clayy = clayy.getSuperclass())
			System.err.println(clayy.getName());
		System.err.println();
		
		Method[] declaredMethods = Fuck.class.getDeclaredMethods();
		
		for(Method method : declaredMethods) {
			
			if(Modifier.isStatic(method.getModifiers()))
				continue;
			
//			Class<?>[] parameterTypes = method.getParameterTypes();
//			for(Class<?> clazz : parameterTypes) {
//				System.err.print(clazz.getName());
//				System.err.print(" ");
//			}
//			System.err.println();
			
//			Type[] genericParameterTypes = method.getGenericParameterTypes();
//			for(Type type : genericParameterTypes) {
//
//				print(type.getClass());
//				if(type instanceof ParameterizedType) {
//					ParameterizedType pt = (ParameterizedType )type;
//					print(pt.getTypeName(), 2);
//					print(pt.getRawType().getTypeName(), 2);
//					Type[] actualTypeArguments = pt.getActualTypeArguments();
//					for(Type passType : actualTypeArguments) {
//						print(type.getClass(), 4);
//						if(passType instanceof ParameterizedType) {
//							ParameterizedType spt = (ParameterizedType )passType;
//							print(spt.getTypeName(), 6);
//							print(spt.getRawType().getTypeName(), 6);
//						} else if (Class.class.equals(passType.getClass())) {
//							print(((Class<?> )passType).getName(), 6);
//						}
//					}
//				} else if (Class.class.equals(type.getClass())) {
//					print(((Class<?> )type).getName(), 2);
//				}
//				print("");
//			}
			print(method.getName());
			Type[] genericParameterTypes = method.getGenericParameterTypes();
			parseType(genericParameterTypes, 1);
			print("");
			print("");
		}
	}
	
	public static void parseType(Type[] types, int level) {
		int accountOfSpace = level * 2;
		for(Type type : types) {
			print(type.getClass(), accountOfSpace);
			accountOfSpace += 2; 
			if(type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType )type;
				print(pt.getTypeName(), accountOfSpace);
				print(pt.getRawType().getTypeName(), accountOfSpace);
				Type[] actualTypeArguments = pt.getActualTypeArguments();
				parseType(actualTypeArguments, level + 2);
			} else if(type instanceof WildcardType) {
				WildcardType wt = (WildcardType )type;
				print(wt.getTypeName(), accountOfSpace);
				print("upperBounds: ", accountOfSpace);
				Type[] upperBounds = wt.getUpperBounds();
				parseType(upperBounds, level + 2);
				print("lowerBounds: ", accountOfSpace);
				Type[] lowerBounds = wt.getLowerBounds();
				parseType(lowerBounds, level + 2);
			} else if(type instanceof GenericArrayType) {
				GenericArrayType gat = (GenericArrayType )type;
				print(gat.getTypeName(), accountOfSpace);
				Type genericComponentType = gat.getGenericComponentType();
				parseType(new Type[] {genericComponentType}, level + 2);
			} else if (Class.class.equals(type.getClass())) {
				print(((Class<?> )type).getName(), accountOfSpace);
			}
			accountOfSpace -= 2; 
		}
	}

	private static void print(Object obj, int sublevel) {
		for(int i=sublevel; i>0; i--)
			System.out.print(' ');
		System.out.println(obj);
	}
	private static void print(Object obj) {
		print(obj, 0);
	}
}
