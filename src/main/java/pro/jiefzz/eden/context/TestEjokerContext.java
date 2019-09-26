package pro.jiefzz.eden.context;

import pro.jiefzz.eden.context.completion.CommandTopicProviders;
import pro.jiefzz.ejoker.commanding.ICommand;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.utils.handlerProviderHelper.RegistCommandHandlerHelper;
import pro.jiefzz.ejoker.utils.handlerProviderHelper.RegistDomainEventHandlerHelper;
import pro.jiefzz.ejoker.utils.handlerProviderHelper.RegistMessageHandlerHelper;
import pro.jiefzz.ejoker.z.context.dev2.impl.EjokerContextDev2Impl;

public class TestEjokerContext {

	public static void main(String[] args) {

		EjokerContextDev2Impl context = new EjokerContextDev2Impl();

		// regist scanner hook
//		context.registeScannerHook((clazz) -> {
//			if (!clazz.getPackage().getName().startsWith("pro.jiefzz")) {
//				// We make sure that CommandHandler and DomainEventHandler will not in E-Joker
//				// Framework package.
////				RegistCommandHandlerHelper.checkAndRegistCommandHandler(clazz);
//				RegistDomainEventHandlerHelper.checkAndRegistDomainEventHandler(clazz);
//			}
//			RegistMessageHandlerHelper.checkAndRegistMessageHandler(context, clazz);
//		});

		context.scanPackage("pro.jiefzz.ejoker.test.context");

		context.refresh();
		
		CommandTopicProviders commandTopicProviders = context.get(CommandTopicProviders.class);
		ITopicProvider<ICommand> iTopicProvider = context.get(ITopicProvider.class, ICommand.class);
		AbstractTopicProvider<ICommand> topicProvider = context.get(AbstractTopicProvider.class, ICommand.class);
		
		System.err.println(commandTopicProviders);
		System.err.println(iTopicProvider);
		System.err.println(topicProvider);
	}

}
