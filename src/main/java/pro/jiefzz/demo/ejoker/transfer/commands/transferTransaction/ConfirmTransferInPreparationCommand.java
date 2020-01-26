package pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认预转入
 * @author kimffy
 *
 */
public class ConfirmTransferInPreparationCommand extends AbstractCommand<String> {
	
	public ConfirmTransferInPreparationCommand() {
	}
	
    public ConfirmTransferInPreparationCommand(String transactionId) {
    	super(transactionId);
    }
}
