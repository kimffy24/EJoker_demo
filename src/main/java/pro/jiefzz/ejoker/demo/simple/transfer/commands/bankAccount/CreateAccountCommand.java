package pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount;

import com.jiefzz.ejoker.commanding.AbstractCommand;

public class CreateAccountCommand extends AbstractCommand {

	private String owner;

	public CreateAccountCommand() {
	}
	
	public CreateAccountCommand(String accountId, String owner) {
		super(accountId);
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

}
