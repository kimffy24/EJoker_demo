package pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.AbstractCommandHandler;
import com.jiefzz.ejoker.commanding.ICommandContext;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.CommandHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.BankAccount;

@EService
@CommandHandler
public class BankAccountCommandHandler extends AbstractCommandHandler {

	private final static Logger logger = LoggerFactory.getLogger(BankAccountCommandHandler.class);
	
//	AtomicLong al1 = new AtomicLong(0);
//	AtomicLong al2 = new AtomicLong(0);
//	
//	public void show1() {
//		logger.error("AddTransactionPreparationCommand hit: {}", al1.get());
//	}
//	
//	public void show2() {
//		logger.error("CommitTransactionPreparationCommand hit: {}", al2.get());
//	}

	public void handle(ICommandContext context, CreateAccountCommand command) {
		context.add(new BankAccount(command.getAggregateRootId(), command.getOwner()));
	}

	public void handle(ICommandContext context, AddTransactionPreparationCommand command) {
//		al1.getAndIncrement();
		BankAccount account;
		account = context.getAsync(command.getAggregateRootId(), BankAccount.class).get();
		account.addTransactionPreparation(command.getTransactionId(), command.getTransactionType(),
				command.getPreparationType(), command.getAmount());
	}

	public void handle(ICommandContext context, CommitTransactionPreparationCommand command) {
//		al2.getAndIncrement();
		BankAccount account;
		account = context.getAsync(command.getAggregateRootId(), BankAccount.class).get();
		account.commitTransactionPreparation(command.getTransactionId());
	}
}
