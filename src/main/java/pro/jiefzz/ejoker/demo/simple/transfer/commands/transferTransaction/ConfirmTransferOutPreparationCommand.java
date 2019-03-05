package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认预转出
 * @author kimffy
 *
 */
public class ConfirmTransferOutPreparationCommand extends AbstractCommand {
	
	public ConfirmTransferOutPreparationCommand() {
	}
	
    public ConfirmTransferOutPreparationCommand(String transactionId) {
    	super(transactionId);
    }
}
