package pro.jiefzz.demo.ejoker.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.domain.domainException.IDomainException;
import pro.jk.ejoker.queue.ITopicProvider;

@EService
public class DomainExceptionMessageTopicProvider implements ITopicProvider<IDomainException> {

	@Override
	public String getTopic(IDomainException source) {
		return TopicReference.ExceptionTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		Set<String> h = new HashSet<>();
		h.add(TopicReference.ExceptionTopic);
		return h;
	}

}
