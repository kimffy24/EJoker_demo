package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.messaging.IApplicationMessage;
import pro.jiefzz.ejoker.queue.ITopicProvider;

@EService
public class ApplicationMessageTopicProvider implements ITopicProvider<IApplicationMessage> {

	@Override
	public String getTopic(IApplicationMessage source) {
		return TopicReference.ApplicationMessageTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		Set<String> h = new HashSet<>();
		h.add(TopicReference.ApplicationMessageTopic);
		return h;
	}

}
