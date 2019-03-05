package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认转入
 * @author kimffy
 *
 */
public class ConfirmTransferInCommand extends AbstractCommand {
	
	public ConfirmTransferInCommand() {
	}
	
    public ConfirmTransferInCommand(String transactionId) {
    	super(transactionId);
    }
}
