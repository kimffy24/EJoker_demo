package pro.jiefzz.ejoker.demo.simple.transfer.equasar;

import pro.jiefzz.ejoker.demo.simple.transfer.TransferAppPerformanceTest;
import pro.jiefzz.ejoker.demo.simple.transfer.TransferPrepare;

/**
 * 
 * env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile org.apache.maven.plugins:maven-dependency-plugin:3.1.1:properties exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx8g -Xmn3g -Dco.paralleluniverse.fibers.verifyInstrumentation=true -javaagent:${co.paralleluniverse:quasar-core:jar} -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.equasar.TransferAppQuasar'
 * @param args
 * @throws Exception
 */
public class TransferAppPerformanceTestQuasar {

	public static void main(String[] args) throws Exception {
		
		TransferAppPerformanceTest.start(TransferPrepare.prepare(new EJokerQuasarBootstrap()));
		
	}
}
