package pro.jiefzz.ejoker_demo.transfer.commandHandlers;

import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.depositTransaction.DepositTransaction;

@EService(type = ESType.COMMAND_HANDLER)
public class DepositTransactionCommandHandlers extends AbstractCommandHandler {

	@Suspendable
	public Future<Void> handleAsync(ICommandContext context, StartDepositTransactionCommand command) {
		return context.addAsync(new DepositTransaction(command.getAggregateRootId(), command.getAccountId(), command.getAmount()));
    }

	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmDepositPreparationCommand command) {
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDepositPreparation();
    }

	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmDepositCommand command) {
        DepositTransaction transaction = await(context.getAsync(command.getAggregateRootId(), DepositTransaction.class));
        transaction.confirmDeposit();
    }
    
}
