package pro.jiefzz.eden.javaGenericExpression.test.A;

import pro.jk.ejoker.eventing.IDomainEvent;

public class X<T extends A<Integer> & IB<?>> extends Y<IDomainEvent<T>> {

}
