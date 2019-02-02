package pro.jiefzz.ejoker.test.javaGenericExpression.testA1;

import com.jiefzz.ejoker.z.common.utils.genericity.GenericExpression;
import com.jiefzz.ejoker.z.common.utils.genericity.GenericExpressionFactory;

public class FuckA1 {

	public static void main(String[] args) {

		GenericExpression genericExpress = GenericExpressionFactory.getGenericExpress(D.class, String.class);
		
		System.err.println(genericExpress);
		
	}
}
