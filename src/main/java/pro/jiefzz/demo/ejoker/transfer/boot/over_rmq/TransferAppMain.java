package pro.jiefzz.demo.ejoker.transfer.boot.over_rmq;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferAppBatch;

public class TransferAppMain extends TransferAppBatch {

	public static void main(String[] args) throws Exception {
		start(new Prepare().getEb());
	}

}
