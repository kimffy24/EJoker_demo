package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransactionInfo;
import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 发起一笔转账交易
 * @author kimffy
 *
 */
public class StartTransferTransactionCommand extends AbstractCommand<String> {

	private TransferTransactionInfo transactionInfo;

	public StartTransferTransactionCommand() {
	}
	
	public StartTransferTransactionCommand(String transactionId, TransferTransactionInfo transactionInfo) {
		super(transactionId);
		this.transactionInfo = transactionInfo;
	}

	public TransferTransactionInfo getTransactionInfo() {
		return transactionInfo;
	}

}
