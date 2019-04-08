package pro.jiefzz.ejoker.test.rocketmq.ejimpl;

import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.bson.types.ObjectId;

import com.jiefzz.ejoker_support.rocketmq.DefaultMQProducer;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

public class TestProducer {

	public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer("ZTestPGroup");
		defaultMQProducer.setNamesrvAddr(EJokerBootstrap.NameServAddr);
		defaultMQProducer.start();
		
		
		for(int i = 0; i< 100; i++)
			try {
				TimeUnit.SECONDS.sleep(5l);
				defaultMQProducer.send(
					new Message("ZTestTopic", "", ObjectId.get().toHexString(), ("" + i).getBytes()),
					
					(mqs, m, a) -> {
						System.err.println(String.format("mqsHash: %d, mqsSize: %d", mqs.hashCode(), mqs.size()));
						return mqs.get((int )System.currentTimeMillis()%mqs.size());
					},
					null
					
					);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		defaultMQProducer.shutdown();
	}
	
}
