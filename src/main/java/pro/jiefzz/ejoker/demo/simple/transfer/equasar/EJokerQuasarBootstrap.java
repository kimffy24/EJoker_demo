package pro.jiefzz.ejoker.demo.simple.transfer.equasar;

import com.jiefzz.equasar.EJoker;

import co.paralleluniverse.fibers.SuspendExecution;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

public class EJokerQuasarBootstrap extends EJokerBootstrap {

	protected EJokerQuasarBootstrap() throws SuspendExecution {
		// 这个类是 com.jiefzz.equasar.EJoker
		// 而不是 com.jiefzz.ejoker.EJoker
		super(EJoker.getInstance());
	}

}
