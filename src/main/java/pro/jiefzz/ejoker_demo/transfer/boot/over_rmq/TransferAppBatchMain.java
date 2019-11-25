package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;

import pro.jiefzz.ejoker_demo.transfer.boot.TransferAppBatch;

public class TransferAppBatchMain extends TransferAppBatch {

	public static void main(String[] args) throws Exception {
		start(new Prepare().getEb());
	}

}
