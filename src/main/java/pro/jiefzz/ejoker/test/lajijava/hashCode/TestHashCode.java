package pro.jiefzz.ejoker.test.lajijava.hashCode;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.A;

public class TestHashCode {

	public static void main(String[] args) {
		
		A<String> a = new A<>();

		A<Boolean> b = new A<>();

		System.err.println(a.hashCode());		
		System.err.println(b.hashCode());
		
	}

}
