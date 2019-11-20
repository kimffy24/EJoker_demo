package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.bootstrap.EJokerBootstrap;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker_demo.transfer.boot.TransferPrepare;
import pro.jiefzz.ejoker_support.rocketmq.MQInstanceHelper;

public class Prepare {
	
	private final static Logger logger = LoggerFactory.getLogger(Prepare.class);
	
	public final static String NameServAddr = "172.16.0.1:9876";
	
	public final static long BatchDelay = 5000l;
	
	public final static String EJokerDefaultImplPackage;
	
	public final static String BusinessPackage = "pro.jiefzz.ejoker_demo.transfer";

	private final EJokerBootstrap eb;
	
	public Prepare() {
		this(BusinessPackage, EJokerDefaultImplPackage);
	}
	
	public Prepare(String... packages) {
		this(new EJokerBootstrap(packages));
	}
	
	protected Prepare(EJokerBootstrap eb) {
		(this.eb = eb)
			.setConsumerInstanceCreator((groupName, eContext) -> MQInstanceHelper.createDefaultMQConsumer(groupName, NameServAddr, eContext))
			.setProducerInstanceCreator((groupName, eContext) -> MQInstanceHelper.createDefaultMQProducer(groupName, NameServAddr, eContext))
			.setPreInitAll(TransferPrepare::prepare)
			.initAll();
	}

	public final IEJokerSimpleContext getEJokerContext() {
		return eb.getEJokerContext();
	}

	public EJokerBootstrap getEb() {
		return eb;
	}
	
	static {
		String ES = null;
		boolean isWindows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
		
		String envKey1 = "ES";
		if(isWindows) {
			// All environment propertie's name will represent by upper case.
//			envKey1 = envKey1.toUpperCase();
		}
		
		Map<String, String> map = System.getenv();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ){
            String key = it.next();
            String value = map.get(key);
            if(envKey1.equals(key))
            	ES = value;
        }
        if(null == ES)
        	ES="";
        switch(ES) {
        case "mongosync":
        case "mongo_sync":
        	ES = "pro.jiefzz.ejoker_demo.support_storage.mongo.mongoSync";
        	break;
        case "":
        default :
        	ES =  "pro.jiefzz.ejoker_support.defaultMemoryImpl";
            break;
        }
        logger.info("Detect ES: {} .", ES);
        EJokerDefaultImplPackage = ES;
	}
	
}
