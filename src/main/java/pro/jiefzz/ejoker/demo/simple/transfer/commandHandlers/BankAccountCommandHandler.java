package pro.jiefzz.ejoker.demo.simple.transfer.commandHandlers;

import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.AbstractCommandHandler;
import com.jiefzz.ejoker.commanding.ICommandContext;
import com.jiefzz.ejoker.infrastructure.varieties.applicationMessage.AbstractApplicationMessage;
import com.jiefzz.ejoker.infrastructure.varieties.applicationMessage.IApplicationMessage;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.CommandHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.demo.simple.transfer.applicationMessageHandlers.AccountValidateFailedMessage;
import pro.jiefzz.ejoker.demo.simple.transfer.applicationMessageHandlers.AccountValidatePassedMessage;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.AddTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CommitTransactionPreparationCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.CreateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.commands.bankAccount.ValidateAccountCommand;
import pro.jiefzz.ejoker.demo.simple.transfer.domain.bankAccount.BankAccount;

@EService
@CommandHandler
public class BankAccountCommandHandler extends AbstractCommandHandler {

	private final static  Logger logger = LoggerFactory.getLogger(BankAccountCommandHandler.class);
			
	@Dependence
	IJSONConverter jsonConverter;
	
	@Suspendable
	public void handle(ICommandContext context, CreateAccountCommand command) {
		context.add(new BankAccount(command.getAggregateRootId(), command.getOwner()));
	}

	@Suspendable
	public SystemFutureWrapper<AsyncTaskResult<IApplicationMessage>> handleAsync(ICommandContext context, ValidateAccountCommand command) {
		IApplicationMessage applicationMessage = new AbstractApplicationMessage() {};
		
		//此处应该会调用外部接口验证账号是否合法，这里仅仅简单通过账号是否以INVALID字符串开头来判断是否合法；根据账号的合法性，返回不同的应用层消息
        if (command.getAggregateRootId().startsWith("INVALID")) {
            applicationMessage = new AccountValidateFailedMessage(command.getAggregateRootId(), command.getTransactionId(), "账户不合法.");
        } else {
            applicationMessage = new AccountValidatePassedMessage(command.getAggregateRootId(), command.getTransactionId());
        }
		return SystemFutureWrapperUtil.createCompleteFutureTask(applicationMessage);
	}

	@Suspendable
	public void handle(ICommandContext context, AddTransactionPreparationCommand command) {
		BankAccount account = await(context.getAsync(command.getAggregateRootId(), BankAccount.class));
		if(null == account) {
			logger.error("account == null, command: {}", jsonConverter.convert(command));
		}
		account.addTransactionPreparation(command.getTransactionId(), command.getTransactionType(),
				command.getPreparationType(), command.getAmount());
	}

	@Suspendable
	public void handle(ICommandContext context, CommitTransactionPreparationCommand command) {
		BankAccount account = await(context.getAsync(command.getAggregateRootId(), BankAccount.class));
		account.commitTransactionPreparation(command.getTransactionId());
	}
}
