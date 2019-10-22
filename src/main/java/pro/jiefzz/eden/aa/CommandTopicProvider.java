package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jiefzz.ejoker.commanding.ICommand;
import pro.jiefzz.ejoker.queue.ITopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class CommandTopicProvider implements ITopicProvider<ICommand> {

	@Override
	public String getTopic(ICommand source) {
		return "CommandTopicProvider";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
