package pro.jiefzz.eden.javaGenericExpression.test.A;

import pro.jiefzz.ejoker.eventing.IDomainEvent;

public class X<T extends A<Integer> & IB<?>> extends Y<IDomainEvent<T>> {

}
