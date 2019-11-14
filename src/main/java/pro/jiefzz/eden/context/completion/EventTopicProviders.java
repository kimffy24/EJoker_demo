package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;

@EService
public class EventTopicProviders extends AbstractTopicProvider<IDomainEvent<?>> {

}
