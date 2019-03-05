package pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

public class ConfirmDepositPreparationCommand extends AbstractCommand {

	public ConfirmDepositPreparationCommand() {
	}

	public ConfirmDepositPreparationCommand(String transactionId) {
		super(transactionId);
	}

}
