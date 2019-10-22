package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.quasar;

import co.paralleluniverse.fibers.SuspendExecution;
import pro.jiefzz.ejoker.EJoker.EJokerSingletonFactory;
import pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.EJokerBootstrap;
import pro.jiefzz.ejoker_support.equasar.EJoker;

public class EJokerQuasarBootstrap extends EJokerBootstrap {

	protected EJokerQuasarBootstrap() throws SuspendExecution {
		// 这个类是 pro.jiefzz.equasar.EJoker
		// 而不是 pro.jiefzz.ejoker.EJoker
		super(new EJokerSingletonFactory(EJoker.class).getInstance());
	}

}
