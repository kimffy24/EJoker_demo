package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue.mqmock;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import com.jiefzz.ejoker.queue.aware.EJokerQueueMessage;

public interface ICQProvider {

	Map<String, Queue<EJokerQueueMessage>> mockMsgQueues = new ConcurrentHashMap<>();;
	
}
