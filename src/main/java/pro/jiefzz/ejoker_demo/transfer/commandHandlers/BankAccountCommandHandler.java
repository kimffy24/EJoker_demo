package pro.jiefzz.ejoker_demo.transfer.commandHandlers;

import static pro.jiefzz.ejoker.z.system.extension.LangUtil.await;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.messaging.AbstractApplicationMessage;
import pro.jiefzz.ejoker.messaging.IApplicationMessage;
import pro.jiefzz.ejoker.z.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.z.context.annotation.context.ESType;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.service.IJSONConverter;
import pro.jiefzz.ejoker_demo.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.ejoker_demo.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker_demo.transfer.commands.bankAccount.ValidateAccountCommand;
import pro.jiefzz.ejoker_demo.transfer.domain.bankAccount.BankAccount;

@EService(type = ESType.COMMAND_HANDLER)
public class BankAccountCommandHandler extends AbstractCommandHandler {

	private final static  Logger logger = LoggerFactory.getLogger(BankAccountCommandHandler.class);
			
	@Dependence
	IJSONConverter jsonConverter;
	
	@Suspendable
	public Future<Void> handleAsync(ICommandContext context, CreateAccountCommand command) {
		return context.addAsync(new BankAccount(command.getAggregateRootId(), command.getOwner()));
	}

	@Suspendable
	public void handleAsync(ICommandContext context, ValidateAccountCommand command) {
		IApplicationMessage applicationMessage = new AbstractApplicationMessage() {};
		
		//此处应该会调用外部接口验证账号是否合法，这里仅仅简单通过账号是否以INVALID字符串开头来判断是否合法；根据账号的合法性，返回不同的应用层消息
        if (command.getAggregateRootId().startsWith("INVALID")) {
            applicationMessage = new AccountValidateFailedMessage(command.getAggregateRootId(), command.getTransactionId(), "账户不合法.");
        } else {
            applicationMessage = new AccountValidatePassedMessage(command.getAggregateRootId(), command.getTransactionId());
        }
        context.setApplicationMessage(applicationMessage);
	}

	@Suspendable
	public void handleAsync(ICommandContext context, AddTransactionPreparationCommand command) {
		BankAccount account = await(context.getAsync(command.getAggregateRootId(), BankAccount.class));
		if(null == account) {
			logger.error("account == null, command: {}", jsonConverter.convert(command));
		}
		account.addTransactionPreparation(
				command.getTransactionId(),
				command.getTransactionType(),
				command.getPreparationType(),
				command.getAmount());
	}

	@Suspendable
	public void handleAsync(ICommandContext context, CommitTransactionPreparationCommand command) {
		BankAccount account = await(context.getAsync(command.getAggregateRootId(), BankAccount.class));
		account.commitTransactionPreparation(command.getTransactionId());
	}
}
