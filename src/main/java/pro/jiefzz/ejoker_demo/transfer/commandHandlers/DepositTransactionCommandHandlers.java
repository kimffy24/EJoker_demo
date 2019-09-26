package pro.jiefzz.ejoker_demo.transfer.commandHandlers;

import static pro.jiefzz.ejoker.z.system.extension.LangUtil.await;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.CommandHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.DepositTransaction;

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
