package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 取消转账交易
 * @author kimffy
 *
 */
public class CancelTransferTransactionCommand extends AbstractCommand<String> {
	
	public CancelTransferTransactionCommand() {
	}
	
    public CancelTransferTransactionCommand(String transactionId) {
    	super(transactionId);
    }
}
