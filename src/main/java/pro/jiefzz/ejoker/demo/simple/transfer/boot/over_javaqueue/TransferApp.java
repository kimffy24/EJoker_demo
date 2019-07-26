package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue;

import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.TransferPrepare;

public class TransferApp {

	public static void main(String[] args) throws Exception {
		pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp.TransferApp.start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
}
