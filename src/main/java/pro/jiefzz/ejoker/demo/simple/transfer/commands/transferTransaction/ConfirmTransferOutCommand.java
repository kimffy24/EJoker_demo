package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认转出
 * @author kimffy
 *
 */
public class ConfirmTransferOutCommand extends AbstractCommand {
	
	public ConfirmTransferOutCommand() {
	}
	
    public ConfirmTransferOutCommand(String transactionId) {
    	super(transactionId);
    }
}
