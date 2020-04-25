package pro.jiefzz.eden.aa;

import java.util.Set;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.domain.domainException.IDomainException;
import pro.jk.ejoker.queue.ITopicProvider;

@EService
public class PublishableExceptionMessageTopicProvider implements ITopicProvider<IDomainException> {

	@Override
	public String getTopic(IDomainException source) {
		return "PublishableExceptionMessageTopicProvider";
	}

	@Override
	public Set<String> GetAllTopics() {
		return null;
	}

}
