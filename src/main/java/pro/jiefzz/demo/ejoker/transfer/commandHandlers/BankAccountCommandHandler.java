package pro.jiefzz.demo.ejoker.transfer.commandHandlers;

import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.demo.ejoker.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.commands.bankAccount.ValidateAccountCommand;
import pro.jiefzz.demo.ejoker.transfer.domain.bankAccount.BankAccount;
import pro.jiefzz.ejoker.commanding.AbstractCommandHandler;
import pro.jiefzz.ejoker.commanding.ICommandContext;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.ESType;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.service.IJSONConverter;
import pro.jiefzz.ejoker.common.system.wrapper.DiscardWrapper;
import pro.jiefzz.ejoker.messaging.AbstractApplicationMessage;
import pro.jiefzz.ejoker.messaging.IApplicationMessage;

@EService(type = ESType.COMMAND_HANDLER)
public class BankAccountCommandHandler extends AbstractCommandHandler {

	private final static  Logger logger = LoggerFactory.getLogger(BankAccountCommandHandler.class);
			
	@Dependence
	IJSONConverter jsonConverter;
	
//	@Suspendable
	public Future<Void> handleAsync(ICommandContext context, CreateAccountCommand command) {
		
//		try {
//			DiscardWrapper.sleep(1000l);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
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
