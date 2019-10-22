package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.domain.domainException.IDomainException;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;

@EService
public class ExceptionTopicProviders extends AbstractTopicProvider<IDomainException> {

}
