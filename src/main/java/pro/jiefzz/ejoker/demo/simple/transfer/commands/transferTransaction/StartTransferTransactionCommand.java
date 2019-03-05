package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransactionInfo;

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
