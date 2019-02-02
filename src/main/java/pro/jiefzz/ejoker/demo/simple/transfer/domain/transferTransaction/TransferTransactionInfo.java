package pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction;

public class TransferTransactionInfo {

	private String sourceAccountId;
	
	private String targetAccountId;
	
	private double amount;

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
