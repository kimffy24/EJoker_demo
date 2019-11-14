package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.messaging.IApplicationMessage;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;

@EService
public class ApplicationMessageTopicProviders extends AbstractTopicProvider<IApplicationMessage> {

}
