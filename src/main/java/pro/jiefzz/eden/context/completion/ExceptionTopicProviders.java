package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.infrastructure.varieties.publishableExceptionMessage.IPublishableException;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class ExceptionTopicProviders extends AbstractTopicProvider<IPublishableException> {

}
