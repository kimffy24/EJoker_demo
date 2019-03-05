package pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers;

import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;

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

	@Suspendable
	public void handle(ICommandContext context, StartDepositTransactionCommand command) {
        context.add(new DepositTransaction(command.getAggregateRootId(), command.getAccountId(), command.getAmount()));
    }

	@Suspendable
    public void handle(ICommandContext context, ConfirmDepositPreparationCommand command) {
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDepositPreparation();
    }

	@Suspendable
    public void handle(ICommandContext context, ConfirmDepositCommand command) {
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDeposit();
    }
    
}
