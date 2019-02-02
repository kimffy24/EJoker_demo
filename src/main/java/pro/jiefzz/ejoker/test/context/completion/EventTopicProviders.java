package pro.jiefzz.ejoker.test.context.completion;

import com.jiefzz.ejoker.eventing.IDomainEvent;
import com.jiefzz.ejoker.queue.AbstractTopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class EventTopicProviders extends AbstractTopicProvider<IDomainEvent<?>> {

}
