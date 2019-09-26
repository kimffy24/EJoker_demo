package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.infrastructure.varieties.applicationMessage.IApplicationMessage;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class ApplicationMessageTopicProviders extends AbstractTopicProvider<IApplicationMessage> {

}
