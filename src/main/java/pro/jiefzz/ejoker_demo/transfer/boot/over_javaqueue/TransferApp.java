package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker_demo.transfer.boot.TransferPrepare;

public class TransferApp {

	public static void main(String[] args) throws Exception {
		pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.TransferApp.start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
}
