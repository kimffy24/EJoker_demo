package pro.jiefzz.ejoker.test.javaGenericExpression.testJ;

import java.math.BigDecimal;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.Y;

public class JDa2 extends JDa1<String, Y<Integer>>{

	public JDa2() {
		super("听说你很牛逼哦！？");
	}
	
	public TestTuple[] testTuple = new TestTuple[2];
	
	BigDecimal testBD = new BigDecimal("3.1415926");
	
}
