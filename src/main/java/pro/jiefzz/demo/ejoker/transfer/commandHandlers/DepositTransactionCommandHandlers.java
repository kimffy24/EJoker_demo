package pro.jiefzz.demo.ejoker.transfer.commandHandlers;

import static pro.jk.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.ConfirmDepositCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.ConfirmDepositPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.depositTransaction.StartDepositTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.depositTransaction.DepositTransaction;
import pro.jk.ejoker.commanding.AbstractCommandHandler;
import pro.jk.ejoker.commanding.ICommandContext;
import pro.jk.ejoker.common.context.annotation.context.ESType;
import pro.jk.ejoker.common.context.annotation.context.EService;

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
