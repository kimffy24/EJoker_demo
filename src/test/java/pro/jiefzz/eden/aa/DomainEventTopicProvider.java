package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.eventing.IDomainEvent;
import pro.jk.ejoker.queue.ITopicProvider;

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
