package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.messaging.IApplicationMessage;
import pro.jk.ejoker.queue.ITopicProvider;

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
