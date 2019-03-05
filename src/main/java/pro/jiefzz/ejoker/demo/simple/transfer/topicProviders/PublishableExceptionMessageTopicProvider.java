package pro.jiefzz.ejoker.demo.simple.transfer.topicProviders;

import java.util.Set;

import com.jiefzz.ejoker.infrastructure.varieties.publishableExceptionMessage.IPublishableException;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.TopicReference;

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
