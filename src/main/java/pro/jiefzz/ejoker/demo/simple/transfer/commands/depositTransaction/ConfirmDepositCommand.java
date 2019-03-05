package pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction;

import com.jiefzz.ejoker.commanding.AbstractCommand;

public class ConfirmDepositCommand extends AbstractCommand {

	public ConfirmDepositCommand() {
	}

	public ConfirmDepositCommand(String transactionId) {
		super(transactionId);
	}

}
