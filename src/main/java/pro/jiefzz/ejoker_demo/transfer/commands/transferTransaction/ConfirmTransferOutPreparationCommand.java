package pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

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
