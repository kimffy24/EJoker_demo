package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.Set;

import pro.jiefzz.ejoker.infrastructure.messaging.varieties.publishableException.IPublishableException;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.boot.TopicReference;

@EService
public class PublishableExceptionMessageTopicProvider implements ITopicProvider<IPublishableException> {

	@Override
	public String getTopic(IPublishableException source) {
		return TopicReference.ExceptionTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
