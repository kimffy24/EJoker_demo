package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.Set;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.domain.domainException.IDomainException;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker_demo.transfer.boot.TopicReference;

@EService
public class PublishableExceptionMessageTopicProvider implements ITopicProvider<IDomainException> {

	@Override
	public String getTopic(IDomainException source) {
		return TopicReference.ExceptionTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
