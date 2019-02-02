package pro.jiefzz.ejoker.test.context.completion;

import com.jiefzz.ejoker.infrastructure.varieties.applicationMessage.IApplicationMessage;
import com.jiefzz.ejoker.queue.AbstractTopicProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;

@EService
public class ApplicationMessageTopicProviders extends AbstractTopicProvider<IApplicationMessage> {

}
