package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jk.ejoker.commanding.AbstractCommand;

/**
 * 确认转入
 * @author kimffy
 *
 */
public class ConfirmTransferInCommand extends AbstractCommand<String> {
	
	public ConfirmTransferInCommand() {
	}
	
    public ConfirmTransferInCommand(String transactionId) {
    	super(transactionId);
    }
}
