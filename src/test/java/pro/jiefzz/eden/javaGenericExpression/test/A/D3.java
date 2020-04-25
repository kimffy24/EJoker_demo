package pro.jiefzz.eden.javaGenericExpression.test.A;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class D3<TX> extends C<Boolean, String> {
	
	protected String[] arr0 = null;
	
	protected C[] arr0_0 = null;

	protected TX[] arr1 = null;
	
	protected IB<Integer>[] arr2 = null;
	
	protected IB<TX>[] arr3 = null;

	protected IB<IB<TX>>[] arr4 = null;
	
	protected IB<TX>[][] arr5 = null;
	
	
	public static void main(String[] args) {
		Field[] declaredFields = D3.class.getDeclaredFields();
		for(Field f:declaredFields) {
//			System.err.println(f.getType().getTypeName());
			Type gType = f.getGenericType();
			System.err.print(gType.getTypeName());
			System.err.print( "  " );
			System.err.print(String.format("%s", gType instanceof Class ? "true" : "false"));
			System.err.print( "  " );
			System.err.println( gType.getClass().getName() );
		}
		
		Field declaredField = null;
		try {
			declaredField = D3.class.getDeclaredField("arr1");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		Type genericType = declaredField.getGenericType();
		
		System.err.println(((GenericArrayType )genericType).getGenericComponentType().getTypeName());
		System.err.println(((GenericArrayType )genericType).getGenericComponentType().getClass().getName());
		
		TypeVariable<Class<String>>[] typeParameters2 = String.class.getTypeParameters();
		System.err.println(typeParameters2.length);
		TypeVariable<Class<D3>>[] typeParameters = D3.class.getTypeParameters();
		System.err.println(typeParameters.length);
		TypeVariable<Class<C>>[] typeParameters3 = C.class.getTypeParameters();
		System.err.println(typeParameters3.length);
		
		
	}
}
