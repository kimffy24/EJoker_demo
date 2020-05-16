package pro.jiefzz.demo.ejoker.transfer.boot.over_rmq;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferApp;

public class TransferAppMain extends TransferApp {

	public static void main(String[] args) throws Exception {
		start(new Prepare().getEb());
	}

}
