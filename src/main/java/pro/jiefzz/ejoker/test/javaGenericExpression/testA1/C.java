package pro.jiefzz.ejoker.test.javaGenericExpression.testA1;

import java.util.Map;

import javax.annotation.Resource;

import com.jiefzz.ejoker.domain.IAggregateRoot;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.IAAAA;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.IB;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.Y;
import pro.jiefzz.ejoker.test.javaGenericExpression.testA.Z;

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
