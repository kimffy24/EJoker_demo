package pro.jiefzz.ejoker_demo.transfer.boot;

import java.util.HashMap;
import java.util.Map;

import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.infrastructure.ITypeNameProvider;
import pro.jiefzz.ejoker.infrastructure.ITypeNameProvider.IDecorator;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.BankAccount;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.DepositTransaction;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransaction;

public class TransferPrepare {

	public static AbstractEJokerBootstrap prepare(AbstractEJokerBootstrap eJokerBootstrap) {

		IEJokerSimpleContext eJokerContext = eJokerBootstrap.getEJokerContext();
		ITypeNameProvider typeNameProvider = eJokerContext.get(ITypeNameProvider.class);
		
		Map<Class<?>, String> aliasMap = new HashMap<>();
		aliasMap.put(BankAccount.class, "BankAccount");
		aliasMap.put(DepositTransaction.class, "DepositTransaction");
		aliasMap.put(TransferTransaction.class, "TransferTransaction");
		typeNameProvider.applyAlias(aliasMap);
		
		typeNameProvider.useDecorator(new IDecorator() {
			
			@Override
			public String preGetType(String typeName) {
				return "pro.jiefzz.ejoker_demo.transfer." + typeName;
			}
			
			@Override
			public String postGetTypeName(String typeName) {
				return typeName.substring("pro.jiefzz.ejoker_demo.transfer.".length());
			}
			
		});
		
		return eJokerBootstrap;
	}
	
}
