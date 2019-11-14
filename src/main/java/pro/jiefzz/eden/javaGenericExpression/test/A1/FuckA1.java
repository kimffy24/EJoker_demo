package pro.jiefzz.eden.javaGenericExpression.test.A1;

import pro.jiefzz.ejoker.common.utils.genericity.GenericExpression;
import pro.jiefzz.ejoker.common.utils.genericity.GenericExpressionFactory;

public class FuckA1 {

	public static void main(String[] args) {

		GenericExpression genericExpress = GenericExpressionFactory.getGenericExpress(D.class, String.class);
		
		System.err.println(genericExpress);
		
	}
}
