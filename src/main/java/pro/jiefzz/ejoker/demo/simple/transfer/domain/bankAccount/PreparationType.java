package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount;

/**
 * 预支出或预收入类型枚举
 */
public enum PreparationType {

	Undefined,
	
	/**
	 * 预支出（记入借方，When your bank debits your account, money is taken from it and paid to someone else.）
	 */
    DebitPreparation,
    /**
     * 预收入（记入贷方，When a sum of money is credited to an account, the bank adds that sum of money to the total in the account.）
     */
    CreditPreparation,
    
}
