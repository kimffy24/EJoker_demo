package pro.jiefzz.eden.context.completion;

import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.domain.domainException.IDomainException;
import pro.jk.ejoker.queue.AbstractTopicProvider;

@EService
public class ExceptionTopicProviders extends AbstractTopicProvider<IDomainException> {

}
