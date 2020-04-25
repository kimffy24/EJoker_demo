package pro.jiefzz.eden.context.completion;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.eventing.IDomainEvent;
import pro.jk.ejoker.queue.AbstractTopicProvider;

@EService
public class EventTopicProviders extends AbstractTopicProvider<IDomainEvent<?>> {

}
