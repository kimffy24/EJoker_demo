package pro.jiefzz.eden.javaGenericExpression.test.A1;

import java.util.Map;

import pro.jiefzz.eden.javaGenericExpression.test.A.IAAAA;
import pro.jiefzz.eden.javaGenericExpression.test.A.IB;
import pro.jiefzz.eden.javaGenericExpression.test.A.Y;
import pro.jiefzz.eden.javaGenericExpression.test.A.Z;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.domain.IAggregateRoot;

public class C<TPc1> extends B<TPc1> {

	@Dependence
	private TPc1 target = null;
	
	private Map<Double, TPc1> source = null;
	
	public int a = 1;
	
	// com.jiefzz.testA.IAAAA<
	// 		[]<
	// 			com.jiefzz.testA.Y<
	// 				java.util.Map<
	// 					java.lang.String,
	// 					? extends 
	// 						[]<
	// 							com.jiefzz.testA.IB<
	// 								com.jiefzz.ejoker.domain.IAggregateRoot
	// 							>
	// 						>
	//				>
	// 			>
	// 		>
	// >
	public IAAAA<Y<Map<String, ? extends IB<IAggregateRoot>[]>>[]> i4a1;
	
	public IAAAA<? extends IB<IAggregateRoot>[]> i4a2;
	
	public IAAAA<TPc1> i4a3;
	
	public Z b = null;
	
	public TPc1 getTarget() {
		return target;
	}
	
}
