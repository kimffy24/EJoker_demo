package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jk.ejoker.commanding.AbstractCommand;

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
