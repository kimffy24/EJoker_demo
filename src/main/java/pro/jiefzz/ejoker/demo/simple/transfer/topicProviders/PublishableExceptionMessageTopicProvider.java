package pro.jiefzz.ejoker.demo.simple.transfer.topicProviders;

import java.util.Set;

import com.jiefzz.ejoker.infrastructure.varieties.publishableExceptionMessage.IPublishableException;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class PublishableExceptionMessageTopicProvider implements ITopicProvider<IPublishableException> {

	@Override
	public String getTopic(IPublishableException source) {
		return "TopicEJokerPublishableException";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
