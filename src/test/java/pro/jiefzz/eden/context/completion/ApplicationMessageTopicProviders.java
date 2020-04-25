package pro.jiefzz.eden.context.completion;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.messaging.IApplicationMessage;
import pro.jk.ejoker.queue.AbstractTopicProvider;

@EService
public class ApplicationMessageTopicProviders extends AbstractTopicProvider<IApplicationMessage> {

}
