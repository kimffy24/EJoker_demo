package pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class ConfirmDepositCommand extends AbstractCommand {

	public ConfirmDepositCommand() {
	}

	public ConfirmDepositCommand(String transactionId) {
		super(transactionId);
	}

}