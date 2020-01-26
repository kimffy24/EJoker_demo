package pro.jiefzz.demo.ejoker.transfer.boot;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TransferConst {

	private final static Logger logger = LoggerFactory.getLogger(TransferConst.class);
	
	public final static long BatchDelay = 5000l;

	public final static String EBusinessPackage = "pro.jiefzz.demo.ejoker.transfer";

	public final static String EJokerDefaultImplPackage;
	
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
        case "mongo":
        case "mongosync":
        case "mongo_sync":
        	ES = "pro.jiefzz.demo.ejoker.storage.mongo";
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
