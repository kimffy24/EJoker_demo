package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker_demo.transfer.boot.TransferPrepare;

public class TransferApp extends pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.TransferApp {

	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
}
