package pro.jiefzz.demo.ejoker.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.eventing.IDomainEvent;
import pro.jk.ejoker.queue.ITopicProvider;

@EService
public class DomainEventTopicProvider implements ITopicProvider<IDomainEvent<?>> {

	@Override
	public String getTopic(IDomainEvent<?> source) {
		return TopicReference.DomainEventTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		Set<String> h = new HashSet<>();
		h.add(TopicReference.DomainEventTopic);
		return h;
	}

}
