package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.TransferPrepare;

public class TransferAppBatch {
	
	public final static int accountAmount = 1000;

	public final static int transferLoop = 200;
	
	public static void main(String[] args) throws Exception {
		pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp.TransferAppBatch.start(TransferPrepare.prepare(new EJokerBootstrap()), accountAmount, transferLoop);
	}

}
