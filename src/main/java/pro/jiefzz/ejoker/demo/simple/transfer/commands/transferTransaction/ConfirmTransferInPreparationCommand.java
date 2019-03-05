package pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

/**
 * 确认预转入
 * @author kimffy
 *
 */
public class ConfirmTransferInPreparationCommand extends AbstractCommand {
	
	public ConfirmTransferInPreparationCommand() {
	}
	
    public ConfirmTransferInPreparationCommand(String transactionId) {
    	super(transactionId);
    }
}
