package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferApp;

/**
 * env EJokerNodeAddr="127.0.0.1" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx4g -Xmn3g -Dpro.jk.ejoker.bootstrap.useQuasar=1 -Dco.paralleluniverse.fibers.verifyInstrumentation=true -javaagent:${co.paralleluniverse:quasar-core:jar:jdk8} -classpath %classpath pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.quasar.TransferAppQuasar'
 * @author kimffy
 *
 */
public class TransferAppMain {

	public static void main(String[] args) throws Exception {
		TransferApp.start(new Prepare().getEb());
	}
	
}
