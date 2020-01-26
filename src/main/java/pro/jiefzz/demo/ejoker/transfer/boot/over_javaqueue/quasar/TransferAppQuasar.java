package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue.quasar;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferApp;

/**
 * 
 * env EJokerNodeAddr="127.0.0.1" mvn -Dmaven.test.skip=true clean compile org.apache.maven.plugins:maven-dependency-plugin:3.1.1:properties exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx4g -Xmn3g -Dco.paralleluniverse.fibers.verifyInstrumentation=true -javaagent:${co.paralleluniverse:quasar-core:jar} -classpath %classpath pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.quasar.TransferAppQuasar'
 * @param args
 * @throws Exception
 */
public class TransferAppQuasar {

	public static void main(String[] args) throws Exception {
		
		TransferApp.start(new EJokerQuasarPrepare().getEb());
		
	}
}
