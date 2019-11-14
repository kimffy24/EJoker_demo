package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.messaging.IApplicationMessage;
import pro.jiefzz.ejoker.queue.ITopicProvider;

@EService
public class ApplicationMessageTopicProvider implements ITopicProvider<IApplicationMessage> {

	@Override
	public String getTopic(IApplicationMessage source) {
		return "ApplicationMessageTopicProvider";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
