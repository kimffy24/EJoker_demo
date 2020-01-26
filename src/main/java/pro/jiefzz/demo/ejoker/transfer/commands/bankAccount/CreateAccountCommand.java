package pro.jiefzz.demo.ejoker.transfer.commands.bankAccount;

import pro.jiefzz.ejoker.commanding.AbstractCommand;

public class CreateAccountCommand extends AbstractCommand<String> {

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
