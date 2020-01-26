package pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction;

/**
 * 值对象，包含了一次转账交易的基本信息
 * @author kimffy
 *
 */
public class TransferTransactionInfo {

	/**
	 * 源账户
	 */
	private String sourceAccountId;
	
	/**
	 * 目标账户
	 */
	private String targetAccountId;
	
	/**
	 * 转账金额
	 */
	private double amount;

	public TransferTransactionInfo() {
		
	}

	public TransferTransactionInfo(String sourceAccountId, String targetAccountId, double amount) {
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.amount = amount;
	}

	public String getSourceAccountId() {
		return sourceAccountId;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public double getAmount() {
		return amount;
	}
	
}
