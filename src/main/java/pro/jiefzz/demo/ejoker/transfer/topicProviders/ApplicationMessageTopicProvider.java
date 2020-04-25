package pro.jiefzz.demo.ejoker.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.messaging.IApplicationMessage;
import pro.jk.ejoker.queue.ITopicProvider;

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
