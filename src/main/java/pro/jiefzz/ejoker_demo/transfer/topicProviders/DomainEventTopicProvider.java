package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.queue.ITopicProvider;

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
