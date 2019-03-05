package pro.jiefzz.ejoker.demo.simple.transfer.topicProviders;

import java.util.Set;

import com.jiefzz.ejoker.commanding.ICommand;
import com.jiefzz.ejoker.queue.ITopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.TopicReference;

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
