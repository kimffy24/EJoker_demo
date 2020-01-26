package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions;

import pro.jiefzz.demo.ejoker.transfer.domain.TransactionType;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.PreparationType;

public class MismatchTransactionPreparationException extends RuntimeException {

	private static final long serialVersionUID = -4312506872031761698L;

	public MismatchTransactionPreparationException(TransactionType transactionType, PreparationType preparationType) {
		super(String.format("Mismatch transaction type [%s] and preparation type [%s].", transactionType,
				preparationType));
	}
}
