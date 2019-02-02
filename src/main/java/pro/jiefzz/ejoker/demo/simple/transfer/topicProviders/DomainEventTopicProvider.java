package pro.jiefzz.ejoker.demo.simple.transfer.topicProviders;

import java.util.Set;

import com.jiefzz.ejoker.eventing.IDomainEvent;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class DomainEventTopicProvider implements ITopicProvider<IDomainEvent<?>> {

	@Override
	public String getTopic(IDomainEvent<?> source) {
		return "TopicEJokerDomainEvent";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
