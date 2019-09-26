package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import pro.jiefzz.ejoker.queue.aware.EJokerQueueMessage;

public interface ICQProvider {

	Map<String, Queue<EJokerQueueMessage>> mockMsgQueues = new ConcurrentHashMap<>();;
	
}
