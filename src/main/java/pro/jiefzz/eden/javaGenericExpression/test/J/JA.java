package pro.jiefzz.eden.javaGenericExpression.test.J;

import java.util.HashMap;
import java.util.Map;

public class JA<Tja1> implements JAI {

	private Tja1 varA1 = null;
	
	private int i = 0;
	
	private Map<Integer, Tja1> fuckMap = new HashMap<>();
	
	public void setVarA1(Tja1 var) {
		this.varA1 = var;
	}
	
	public void addValue(Tja1 var) {
		fuckMap.put(++i, var);
	}
	
}
