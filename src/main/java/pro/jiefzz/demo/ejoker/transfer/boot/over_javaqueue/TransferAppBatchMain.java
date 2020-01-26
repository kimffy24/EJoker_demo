package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferAppBatch;

public class TransferAppBatchMain {
	
	public static void main(String[] args) throws Exception {
		TransferAppBatch.start(new Prepare().getEb());
	}

}
