package pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 发起一笔存款交易
 * @author kimffy
 *
 */
public class StartDepositTransactionCommand extends AbstractCommand<String> {

	/**
	 * 账户ID
	 */
	private String accountId;

	/**
	 * 存款金额
	 */
	private double amount;

	public StartDepositTransactionCommand() {
	}

	public StartDepositTransactionCommand(String transactionId, String accountId, double amount) {
		super(transactionId);
		this.accountId = accountId;
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public double getAmount() {
		return amount;
	}

}
