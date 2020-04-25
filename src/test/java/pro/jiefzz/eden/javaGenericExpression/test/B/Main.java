//package pro.jiefzz.eden.javaGenericExpression.test.B;
//
//import java.math.BigDecimal;
//
//import pro.jiefzz.eden.javaGenericExpression.test.A.D0;
//import pro.jiefzz.eden.javaGenericExpression.test.A.D1;
//import pro.jiefzz.eden.javaGenericExpression.test.A.D2;
//import pro.jiefzz.ejoker.z.utils.genericity.GenericDefination;
//import pro.jiefzz.ejoker.z.utils.genericity.GenericExpression;
//import pro.jiefzz.ejoker.z.utils.genericity.GenericExpressionFactory;
//
//public class Main {
//
//	@SuppressWarnings("unused")
//	public static void main(String[] args) {
//
//		GenericExpression genericExpressm = GenericExpressionFactory.getMiddleStatementGenericExpression(D0.class);
//		GenericExpression genericExpress = GenericExpressionFactory.getGenericExpress(D0.class, BigDecimal.class);
//		
//		GenericDefination D0Meta = GenericDefination.getOrCreateDefination(D0.class);
//		GenericDefination D1Meta = GenericDefination.getOrCreateDefination(D1.class);
//		GenericDefination D2Meta = GenericDefination.getOrCreateDefination(D2.class);
//		
//		GenericDefination orCreateMeta = GenericDefination.getOrCreateDefination(D0.class);
//		
//		System.err.println(orCreateMeta);
//		System.err.println(genericExpress);
//		
//	}
//
//}
