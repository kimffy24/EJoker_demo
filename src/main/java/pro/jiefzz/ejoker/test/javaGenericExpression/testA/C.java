package pro.jiefzz.ejoker.test.javaGenericExpression.testA;

import java.util.List;
import java.util.Map;

import com.jiefzz.ejoker.domain.IAggregateRoot;

public class C<TM1, TM2> extends B<TM2, TM1> implements IAAA, IB<TM1> {

	protected TM1 varC = null;

	protected List<TM1> listC1 = null;
	
	protected List<TM2> listC2 = null;

	protected Map<String, TM2> mapC1 = null;
	
	protected Map<TM1, TM2> mapC2 = null;
	
	protected Map testMap = null;

	@Override
	public TM1 getValueIB() {
		return null;
	}

	@Override
	public Y<Map<String, ? extends IB<IAggregateRoot>[]>>[] getValueIA4() {
		return null;
	}

	@Override
	public void applyValueIA4(Y<Map<String, ? extends IB<IAggregateRoot>[]>>[] target) {
		
	}
}