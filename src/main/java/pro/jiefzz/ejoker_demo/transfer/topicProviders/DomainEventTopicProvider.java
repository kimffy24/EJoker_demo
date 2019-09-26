package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.Set;

import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.boot.TopicReference;

@EService
public class DomainEventTopicProvider implements ITopicProvider<IDomainEvent<?>> {

	@Override
	public String getTopic(IDomainEvent<?> source) {
		return TopicReference.DomainEventTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
