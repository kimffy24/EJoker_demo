package pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction;

import pro.jk.ejoker.commanding.AbstractCommand;

public class ConfirmDepositCommand extends AbstractCommand<String> {

	public ConfirmDepositCommand() {
	}

	public ConfirmDepositCommand(String transactionId) {
		super(transactionId);
	}

}
