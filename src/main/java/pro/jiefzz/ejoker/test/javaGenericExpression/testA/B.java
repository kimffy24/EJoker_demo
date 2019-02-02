package pro.jiefzz.ejoker.test.javaGenericExpression.testA;

import java.util.List;
import java.util.Map;

public class B<TSA, TSB> extends A<TSB> {

	protected TSA varB1 = null;
	
	protected TSB varB2 = null;

	protected List<TSB> listB = null;

	protected Map<String, TSB> mapB = null;
}
