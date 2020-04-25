package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferAppBatch;

/**
 * env EJokerNodeAddr="127.0.0.1" mvn -Dmaven.test.skip=true clean compile org.apache.maven.plugins:maven-dependency-plugin:3.1.1:properties exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx4g -Xmn3g -Dpro.jk.ejoker.bootstrap.useQuasar=1 -Dco.paralleluniverse.fibers.verifyInstrumentation=true -javaagent:${co.paralleluniverse:quasar-core:jar} -classpath %classpath pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue.TransferAppBatchMain'
 * @author kimffy
 *
 */
public class TransferAppBatchMain {
	
	public static void main(String[] args) throws Exception {
		TransferAppBatch.start(new Prepare().getEb());
	}

}
