package pro.jiefzz.ejoker.test.javaGenericExpression.testB;

import java.math.BigDecimal;

import com.jiefzz.ejoker.z.common.utils.genericity.GenericDefination;
import com.jiefzz.ejoker.z.common.utils.genericity.GenericExpression;
import com.jiefzz.ejoker.z.common.utils.genericity.GenericExpressionFactory;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.D0;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.D1;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.D2;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		GenericExpression genericExpressm = GenericExpressionFactory.getMiddleStatementGenericExpression(D0.class);
		GenericExpression genericExpress = GenericExpressionFactory.getGenericExpress(D0.class, BigDecimal.class);
		
		GenericDefination D0Meta = GenericDefination.getOrCreateDefination(D0.class);
		GenericDefination D1Meta = GenericDefination.getOrCreateDefination(D1.class);
		GenericDefination D2Meta = GenericDefination.getOrCreateDefination(D2.class);
		
		GenericDefination orCreateMeta = GenericDefination.getOrCreateDefination(D0.class);
		
		System.err.println(orCreateMeta);
		System.err.println(genericExpress);
		
	}

}
