package pro.jiefzz.ejoker.test.javaGenericExpression.testJ;

public class TestTuple2<TTT1> {

	public TTT1 ttt= null;
	
	public int[][] aaaa = null;

	public TestTuple2() {
	}
	
	public TestTuple2(TTT1 init) {
		this.ttt = init;
		
		aaaa = new int[][] { new int[] {1, 3, 5, 7}, new int[] {2, 4 ,8,16, 32, 64}};
		
	}
	
}
