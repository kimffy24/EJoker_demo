package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.HashMap;
import java.util.Map;

import com.jiefzz.ejoker.infrastructure.ITypeNameProvider;
import com.jiefzz.ejoker.infrastructure.ITypeNameProvider.IDecorator;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.BankAccount;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.DepositTransaction;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransaction;

public class TransferPrepare {

	public static EJokerBootstrap prepare(EJokerBootstrap eJokerBootstrap) {

		IEJokerSimpleContext eJokerContext = eJokerBootstrap.getEJokerContext();
		ITypeNameProvider typeNameProvider = eJokerContext.get(ITypeNameProvider.class);
		
		Map<Class<?>, String> aliasMap = new HashMap<>();
		aliasMap.put(BankAccount.class, "BankAccount");
		aliasMap.put(DepositTransaction.class, "DepositTransaction");
		aliasMap.put(TransferTransaction.class, "TransferTransaction");
		typeNameProvider.applyDictionary(aliasMap);
		
		typeNameProvider.applyDecorator(new IDecorator() {
			
			@Override
			public String preGetType(String typeName) {
				return "pro.jiefzz.ejoker.demo.simple.transfer." + typeName;
			}
			
			@Override
			public String postGetTypeName(String typeName) {
				return typeName.substring("pro.jiefzz.ejoker.demo.simple.transfer.".length());
			}
			
		});
		
		return eJokerBootstrap;
	}
	
}
