package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker_demo.transfer.boot.TransferAppBatch;

public class TransferAppBatchMain {
	
	public static void main(String[] args) throws Exception {
		TransferAppBatch.start(new Prepare().getEb());
	}

}
