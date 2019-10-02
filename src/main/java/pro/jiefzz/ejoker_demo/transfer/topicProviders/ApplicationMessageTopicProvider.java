package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.Set;

import pro.jiefzz.ejoker.infrastructure.messaging.varieties.applicationMessage.IApplicationMessage;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker_demo.transfer.boot.TopicReference;

@EService
public class ApplicationMessageTopicProvider implements ITopicProvider<IApplicationMessage> {

	@Override
	public String getTopic(IApplicationMessage source) {
		return TopicReference.ApplicationMessageTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
