package pro.jiefzz.ejoker.demo.simple.transfer.equasar;

import com.jiefzz.equasar.EJoker;

import co.paralleluniverse.fibers.SuspendExecution;
import pro.jiefzz.ejoker.demo.simple.transfer.EJokerBootstrap;

public class EJokerQuasarBootstrap extends EJokerBootstrap {

	protected EJokerQuasarBootstrap() throws SuspendExecution {
		super(EJoker.getInstance());
	}

}
