package pro.jiefzz.demo.ejoker.transfer.boot;

import java.util.HashMap;
import java.util.Map;

import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.BankAccount;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.DepositTransaction;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransaction;
import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker.infrastructure.ITypeNameProvider;
import pro.jiefzz.ejoker.infrastructure.ITypeNameProvider.IDecorator;

public class TransferPrepare {

	public static void prepare(IEJokerSimpleContext eJokerContext) {
		ITypeNameProvider typeNameProvider = eJokerContext.get(ITypeNameProvider.class);
		
		Map<Class<?>, String> aliasMap = new HashMap<>();
		aliasMap.put(BankAccount.class, "BankAccount");
		aliasMap.put(DepositTransaction.class, "DepositTransaction");
		aliasMap.put(TransferTransaction.class, "TransferTransaction");
		typeNameProvider.applyAlias(aliasMap);
		
		typeNameProvider.useDecorator(new IDecorator() {
			
			@Override
			public String preGetType(String typeName) {
				return "pro.jiefzz.demo.ejoker.transfer." + typeName;
			}
			
			@Override
			public String postGetTypeName(String typeName) {
				return typeName.substring("pro.jiefzz.demo.ejoker.transfer.".length());
			}
			
		});
	}
	
}
