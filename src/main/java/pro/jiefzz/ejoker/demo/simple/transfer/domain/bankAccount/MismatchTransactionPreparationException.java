package pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.TransactionType;

public class MismatchTransactionPreparationException extends RuntimeException {

	private static final long serialVersionUID = -4312506872031761698L;

	public MismatchTransactionPreparationException(TransactionType transactionType, PreparationType preparationType) {
		super(String.format("Mismatch transaction type [%s] and preparation type [%s].", transactionType,
				preparationType));
	}
}
