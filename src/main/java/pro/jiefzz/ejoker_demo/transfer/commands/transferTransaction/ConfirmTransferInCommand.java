package pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

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
