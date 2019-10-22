package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class DomainEventTopicProvider implements ITopicProvider<IDomainEvent<?>> {

	@Override
	public String getTopic(IDomainEvent<?> source) {
		return "DomainEventTopicProvider";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
