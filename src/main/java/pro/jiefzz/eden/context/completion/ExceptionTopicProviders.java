package pro.jiefzz.eden.context.completion;

import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.domain.domainException.IDomainException;
import pro.jiefzz.ejoker.queue.AbstractTopicProvider;

@EService
public class ExceptionTopicProviders extends AbstractTopicProvider<IDomainException> {

}
