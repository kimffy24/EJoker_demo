package pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers;

import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;

import com.jiefzz.ejoker.commanding.AbstractCommandHandler;
import com.jiefzz.ejoker.commanding.ICommandContext;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.CommandHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.CancelTransferTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.ConfirmAccountValidatePassedCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.ConfirmTransferInCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.ConfirmTransferInPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.ConfirmTransferOutCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.ConfirmTransferOutPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.transferTransaction.StartTransferTransactionCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.transferTransaction.TransferTransaction;

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
