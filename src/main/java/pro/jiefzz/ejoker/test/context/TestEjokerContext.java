package pro.jiefzz.ejoker.test.context;

import com.jiefzz.ejoker.commanding.ICommand;
import com.jiefzz.ejoker.queue.AbstractTopicProvider;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.utils.handlerProviderHelper.RegistCommandHandlerHelper;
import com.jiefzz.ejoker.utils.handlerProviderHelper.RegistDomainEventHandlerHelper;
import com.jiefzz.ejoker.utils.handlerProviderHelper.RegistMessageHandlerHelper;
import com.jiefzz.ejoker.z.common.context.dev2.impl.EjokerContextDev2Impl;

import pro.jiefzz.ejoker.test.context.completion.CommandTopicProviders;

public class TestEjokerContext {

	public static final String SELF_PACNAGE_NAME = "com.jiefzz.ejoker";

	public static void main(String[] args) {

		EjokerContextDev2Impl context = new EjokerContextDev2Impl();

		// regist scanner hook
		context.registeScannerHook((clazz) -> {
			if (!clazz.getPackage().getName().startsWith(SELF_PACNAGE_NAME)) {
				// We make sure that CommandHandler and DomainEventHandler will not in E-Joker
				// Framework package.
//				RegistCommandHandlerHelper.checkAndRegistCommandHandler(clazz);
				RegistDomainEventHandlerHelper.checkAndRegistDomainEventHandler(clazz);
			}
			RegistMessageHandlerHelper.checkAndRegistMessageHandler(clazz);
		});

		context.scanPackage("pro.jiefzz.ejoker.test.context");
		context.scanPackage(SELF_PACNAGE_NAME);

		context.refresh();
		
		CommandTopicProviders commandTopicProviders = context.get(CommandTopicProviders.class);
		ITopicProvider<ICommand> iTopicProvider = context.get(ITopicProvider.class, ICommand.class);
		AbstractTopicProvider<ICommand> topicProvider = context.get(AbstractTopicProvider.class, ICommand.class);
		
		System.err.println(commandTopicProviders);
		System.err.println(iTopicProvider);
		System.err.println(topicProvider);
	}

}
