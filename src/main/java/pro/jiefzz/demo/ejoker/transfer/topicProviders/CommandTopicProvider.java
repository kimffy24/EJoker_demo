package pro.jiefzz.demo.ejoker.transfer.topicProviders;

import java.util.HashSet;
import java.util.Set;

import pro.jk.ejoker.commanding.ICommand;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.queue.ITopicProvider;

@EService
public class CommandTopicProvider implements ITopicProvider<ICommand> {

	@Override
	public String getTopic(ICommand source) {
		return TopicReference.CommandTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		Set<String> h = new HashSet<>();
		h.add(TopicReference.CommandTopic);
		return h;
	}

}
