package pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransactionInfo;

/**
 * 发起一笔转账交易
 * @author kimffy
 *
 */
public class StartTransferTransactionCommand extends AbstractCommand {

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
