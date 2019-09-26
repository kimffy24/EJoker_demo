package pro.jiefzz.ejoker_demo.transfer.commandHandlers;

import static pro.jiefzz.ejoker.z.system.extension.LangUtil.await;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.z.context.annotation.assemblies.CommandHandler;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.CancelTransferTransactionCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.ConfirmAccountValidatePassedCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.ConfirmTransferInCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.ConfirmTransferInPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.ConfirmTransferOutCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.ConfirmTransferOutPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.transferTransaction.StartTransferTransactionCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.transferTransaction.TransferTransaction;

@EService
@CommandHandler
public class TransferTransactionCommandHandlers extends AbstractCommandHandler {

	@Suspendable
	public void handle(ICommandContext context, StartTransferTransactionCommand command) {
        context.add(new TransferTransaction(command.getAggregateRootId(), command.getTransactionInfo()));
    }

	@Suspendable
    public void handle(ICommandContext context, ConfirmAccountValidatePassedCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmAccountValidatePassed(command.getAccountId());
    }
	
	@Suspendable
    public void handle(ICommandContext context, ConfirmTransferOutPreparationCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferOutPreparation();
    }
	
	@Suspendable
    public void handle(ICommandContext context, ConfirmTransferInPreparationCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferInPreparation();
    }
	
	@Suspendable
    public void handle(ICommandContext context, ConfirmTransferOutCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferOut();
    }
	
	@Suspendable
    public void handle(ICommandContext context, ConfirmTransferInCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.confirmTransferIn();
    }
	
	@Suspendable
    public void handle(ICommandContext context, CancelTransferTransactionCommand command) {
		TransferTransaction transaction = await(context.getAsync(command.getAggregateRootId(), TransferTransaction.class));
        transaction.cancel();
    }
}
