package pro.jiefzz.eden.javaGenericExpression.test.A;

public class Y<TY1> {
	
	private TY1 chushizhi = null;
	
	ZEnum eValue = null;

	public Y() {}
	
	public Y(TY1 chushizhi) {
		
		if(System.currentTimeMillis()%2 == 0)
			eValue = ZEnum.FIRST;
		else
			eValue = ZEnum.SECOND;
		
		this.chushizhi = chushizhi;
	}
	
	public TY1 getChushizhi() {
		return chushizhi;
	}
}
