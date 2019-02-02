package pro.jiefzz.ejoker.test.context.completion;

import com.jiefzz.ejoker.infrastructure.varieties.publishableExceptionMessage.IPublishableException;
import com.jiefzz.ejoker.queue.AbstractTopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class ExceptionTopicProviders extends AbstractTopicProvider<IPublishableException> {

}
