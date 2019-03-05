package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionType;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.exceptions.MismatchTransactionPreparationException;

/**
 * 实体，表示账户聚合内的一笔预操作（如预存款、预取款、预转入、预转出）
 * 
 * @author kimffy
 *
 */
public class TransactionPreparation {
	/**
	 * 账户ID
	 */
	private String accountId;

	/**
	 * 交易ID
	 */
	private String transactionId;

	/**
	 * 预借或预贷
	 */
	private PreparationType preparationType;

	/**
	 * 交易类型
	 */
	private TransactionType transactionType;

	/**
	 * 交易金额
	 */
	private double amount;

	public TransactionPreparation() {
	}
	
	public TransactionPreparation(String accountId, String transactionId, TransactionType transactionType,
			PreparationType preparationType, double amount) {
		if (transactionType == TransactionType.DepositTransaction && !PreparationType.CreditPreparation.equals(preparationType)) {
			throw new MismatchTransactionPreparationException(transactionType, preparationType);
		}
		if (transactionType == TransactionType.WithdrawTransaction && !PreparationType.DebitPreparation.equals(preparationType)) {
			throw new MismatchTransactionPreparationException(transactionType, preparationType);
		}
		this.accountId = accountId;
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.preparationType = preparationType;
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public PreparationType getPreparationType() {
		return preparationType;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public double getAmount() {
		return amount;
	}

}
