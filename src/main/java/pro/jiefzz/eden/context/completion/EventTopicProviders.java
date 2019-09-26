package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class EventTopicProviders extends AbstractTopicProvider<IDomainEvent<?>> {

}
