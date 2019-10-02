package pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认转出
 * @author kimffy
 *
 */
public class ConfirmTransferOutCommand extends AbstractCommand<String> {
	
	public ConfirmTransferOutCommand() {
	}
	
    public ConfirmTransferOutCommand(String transactionId) {
    	super(transactionId);
    }
}
