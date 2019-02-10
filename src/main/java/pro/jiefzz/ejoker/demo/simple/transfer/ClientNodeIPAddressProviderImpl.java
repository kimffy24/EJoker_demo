package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.Iterator;
import java.util.Map;

import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.rpc.IClientNodeIPAddressProvider;

@EService
public class ClientNodeIPAddressProviderImpl implements IClientNodeIPAddressProvider {

	@Override
	public String getClientNodeIPAddress() {
		return EJokerNodeAddr;
	}

	static {
		String eJokerNodeAddrTmp = null;
		
		Map<String, String> map = System.getenv();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ){
            String key = it.next();
            String value = map.get(key);
            if("EJokerNodeAddr".equals(key))
            	eJokerNodeAddrTmp = value;
        }
        
        if(null == eJokerNodeAddrTmp || "".equals(eJokerNodeAddrTmp)) {
        	eJokerNodeAddrTmp = "192.168.1.10";
        }
        
        EJokerNodeAddr = eJokerNodeAddrTmp;
	}
	
	private final static String EJokerNodeAddr;
	
}
