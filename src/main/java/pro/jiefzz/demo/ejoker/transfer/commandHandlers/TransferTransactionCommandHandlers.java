package pro.jiefzz.demo.ejoker.transfer.commandHandlers;

import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.CancelTransferTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmAccountValidatePassedCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferInCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferInPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferOutCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.ConfirmTransferOutPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.transferTransaction.StartTransferTransactionCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.transferTransaction.TransferTransaction;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;

@EService(type = ESType.COMMAND_HANDLER)
public class TransferTransactionCommandHandlers extends AbstractCommandHandler {

	@Suspendable
	public Future<Void> handleAsync(ICommandContext context, StartTransferTransactionCommand command) {
		return context.addAsync(new TransferTransaction(command.getAggregateRootId(), command.getTransactionInfo()));
    }

	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmAccountValidatePassedCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmAccountValidatePassed(command.getAccountId());
    }
	
	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmTransferOutPreparationCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferOutPreparation();
    }
	
	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmTransferInPreparationCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferInPreparation();
    }
	
	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmTransferOutCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferOut();
    }
	
	@Suspendable
    public void handleAsync(ICommandContext context, ConfirmTransferInCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferIn();
    }
	
	@Suspendable
    public void handleAsync(ICommandContext context, CancelTransferTransactionCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.cancel();
    }
}
