package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 取消转账交易
 * @author kimffy
 *
 */
public class CancelTransferTransactionCommand extends AbstractCommand {
	
	public CancelTransferTransactionCommand() {
	}
	
    public CancelTransferTransactionCommand(String transactionId) {
    	super(transactionId);
    }
}
