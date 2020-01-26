package pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.exceptions;

public class TransactionPreparationNotExistException extends RuntimeException {

	private static final long serialVersionUID = -9159184237614412139L;

	public TransactionPreparationNotExistException(String accountId, String transactionId) {
		super(String.format("TransactionPreparation[transactionId={%s}] not exist in account[id={%s}].", transactionId,
				accountId));
	}
}
