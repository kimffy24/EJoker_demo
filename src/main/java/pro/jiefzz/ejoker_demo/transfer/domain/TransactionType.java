package pro.jiefzz.ejoker_demo.transfer.domain;

/**
 * 交易类型枚举
 * @author kimffy
 */
public enum TransactionType {

	Undefined,
	
	/**
	 * 存款
	 */
	DepositTransaction,
	
	/**
	 * 取款
	 */
	WithdrawTransaction,
    
	/**
	 * 转账
	 */
	TransferTransaction,
    
	;
	
}
