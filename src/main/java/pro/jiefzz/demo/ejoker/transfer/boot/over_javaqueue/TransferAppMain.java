package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferApp;

public class TransferAppMain {

	public static void main(String[] args) throws Exception {
		TransferApp.start(new Prepare().getEb());
	}
	
}
