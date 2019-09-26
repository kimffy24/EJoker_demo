package pro.jiefzz.ejoker_demo.transfer;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.rpc.IClientNodeIPAddressProvider;

@EService
public class ClientNodeIPAddressProviderImpl implements IClientNodeIPAddressProvider {

	private final static Logger logger = LoggerFactory.getLogger(ClientNodeIPAddressProviderImpl.class);
	
	@Override
	public String getClientNodeIPAddress() {
		return EJokerNodeAddr;
	}

	static {
		String eJokerNodeAddrTmp = null;
		boolean isWindows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
		
		String envKey = "EJokerNodeAddr";
		if(isWindows) {
			// All environment propertie's name will represent by upper case.
			envKey = envKey.toUpperCase();
		}
		
		Map<String, String> map = System.getenv();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ){
            String key = it.next();
            String value = map.get(key);
            if(envKey.equals(key))
            	eJokerNodeAddrTmp = value;
        }
        
        if(null == eJokerNodeAddrTmp || "".equals(eJokerNodeAddrTmp)) {
        	eJokerNodeAddrTmp = "127.0.0.1";
        }
        logger.info("Detect node address: {}.", eJokerNodeAddrTmp);
        EJokerNodeAddr = eJokerNodeAddrTmp;
	}
	
	private final static String EJokerNodeAddr;
}
