package pro.jiefzz.ejoker.demo.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.jiefzz.ejoker.eventing.IDomainEvent;
import com.jiefzz.ejoker.eventing.IEventSerializer;
import com.jiefzz.ejoker.infrastructure.ITypeNameProvider;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.service.IJSONConverter;

@EService
public class EventStreamMongoSerializer implements IEventSerializer {

	@Dependence
	private IJSONConverter jsonSerializer;
	
	@Dependence
	private ITypeNameProvider typeNameProvider;
	
	@Override
	public Map<String, String> serializer(Collection<IDomainEvent<?>> events) {
		Map<String, String> dict = new LinkedHashMap<String, String>();
		for(IDomainEvent<?> event:events)
			dict.put(typeNameProvider.getTypeName(event.getClass()).replaceAll("\\.", "_"), jsonSerializer.convert(event));
		return dict;
	}

	@Override
	public List<IDomainEvent<?>> deserializer(Map<String, String> data) {
		List<IDomainEvent<?>> list = new ArrayList<IDomainEvent<?>>();
		Set<Entry<String,String>> entrySet = data.entrySet();
		for(Entry<String,String> entry:entrySet) {
			Class<?> eventType = typeNameProvider.getType(entry.getKey().replaceAll("_", "\\."));
			Object revert = jsonSerializer.revert(entry.getValue(), eventType);
			list.add((IDomainEvent<?> )revert);
		}
		return list;
	}

}
