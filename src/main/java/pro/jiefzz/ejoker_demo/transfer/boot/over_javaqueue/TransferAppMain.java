package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker_demo.transfer.boot.TransferApp;

public class TransferAppMain {

	public static void main(String[] args) throws Exception {
		TransferApp.start(new Prepare().getEb());
	}
	
}
