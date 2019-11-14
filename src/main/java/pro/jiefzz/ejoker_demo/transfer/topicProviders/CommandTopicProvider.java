package pro.jiefzz.ejoker_demo.transfer.topicProviders;

import java.util.Set;

import pro.jiefzz.ejoker.commanding.ICommand;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker_demo.transfer.boot.TopicReference;

@EService
public class CommandTopicProvider implements ITopicProvider<ICommand> {

	@Override
	public String getTopic(ICommand source) {
		return TopicReference.CommandTopic;
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
