package pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers.bankAccount;

import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.AbstractCommandHandler;
import com.jiefzz.ejoker.commanding.ICommandContext;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.CommandHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.depositTransaction.DepositTransaction;

@EService
@CommandHandler
public class DepositTransactionCommandHandlers extends AbstractCommandHandler {

	private final static Logger logger = LoggerFactory.getLogger(DepositTransactionCommandHandlers.class);

	AtomicLong al1 = new AtomicLong(0);
	AtomicLong al2 = new AtomicLong(0);
	AtomicLong al3 = new AtomicLong(0);
	
	public void show1() {
		logger.error("StartDepositTransactionCommand hit: {}", al1.get());
	}
	
	public void show2() {
		logger.error("ConfirmDepositPreparationCommand hit: {}", al2.get());
	}

	public void show3() {
		logger.error("ConfirmDepositCommand hit: {}", al3.get());
	}

	@Suspendable
	public void handle(ICommandContext context, StartDepositTransactionCommand command) {
		al1.getAndIncrement();
        context.add(new DepositTransaction(command.getAggregateRootId(), command.getAccountId(), command.getAmount()));
    }

	@Suspendable
    public void handle(ICommandContext context, ConfirmDepositPreparationCommand command) {
		al2.getAndIncrement();
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDepositPreparation();
    }

	@Suspendable
    public void handle(ICommandContext context, ConfirmDepositCommand command) {
		al3.getAndIncrement();
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDeposit();
    }
    
}
