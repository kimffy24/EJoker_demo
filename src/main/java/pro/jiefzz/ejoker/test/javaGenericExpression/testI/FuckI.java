package pro.jiefzz.ejoker.test.javaGenericExpression.testI;

import java.lang.reflect.Array;

public class FuckI {
	
	public static void main(String[] args) {
		
		int[][][] x = new int[][][] {};
		
		System.err.println(x.getClass());
		
		System.err.println(x.getClass().getComponentType());
		System.err.println(x.getClass().getComponentType().getComponentType());
		System.err.println(x.getClass().getComponentType().getComponentType().getComponentType());
		
		System.err.println(Array.newInstance(x.getClass().getComponentType(), 2).getClass());
	}
	
}
