package pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class ConfirmDepositPreparationCommand extends AbstractCommand<String> {

	public ConfirmDepositPreparationCommand() {
	}

	public ConfirmDepositPreparationCommand(String transactionId) {
		super(transactionId);
	}

}
