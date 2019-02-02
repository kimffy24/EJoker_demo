package pro.jiefzz.ejoker.test.javaGenericExpression.testJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDa1<TPd1, TPd2> {

	public static int index = 0;
	
	private List<Map<String, TPd2>> container = new ArrayList<>();
	
	private JA<TPd1> important = new JA<>();
	
	public JDa1(TPd1 init) {
		
		important.setVarA1(init);
		
		container.add(new HashMap<>());
		container.add(new HashMap<>());
		
	}
	
	public void addValue(TPd2 var) {
		Map<String, TPd2> map = container.get((++index)%2);
		map.put("" +System.currentTimeMillis(), var);
	}

	public void addValueToImportant(TPd1 var) {
		important.addValue(var);
	}

	public TestTuple2<TPd2>[] testTuple2 = new TestTuple2[2];
	
	private static int arrayIndex = 0;
	
	public void addTestTuple2(TestTuple2<TPd2> tuple) {
		testTuple2[(arrayIndex++)%2] = tuple;
	}
}
