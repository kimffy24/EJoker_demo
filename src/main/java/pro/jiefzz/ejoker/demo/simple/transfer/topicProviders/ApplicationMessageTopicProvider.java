package pro.jiefzz.ejoker.demo.simple.transfer.topicProviders;

import java.util.Set;

import com.jiefzz.ejoker.infrastructure.varieties.applicationMessage.IApplicationMessage;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class ApplicationMessageTopicProvider implements ITopicProvider<IApplicationMessage> {

	@Override
	public String getTopic(IApplicationMessage source) {
		return "TopicEJokerApplicationMessage";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
