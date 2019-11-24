package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.Prepare;

public class TransferAppBatch extends pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.TransferAppBatch {
	
	public static void main(String[] args) throws Exception {
		start(new Prepare().getEb(), getAccountAmount(), getDepositLoop());
	}

}
