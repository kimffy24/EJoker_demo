package pro.jiefzz.demo.ejoker.storage.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import co.paralleluniverse.fibers.Suspendable;
import pro.jk.ejoker.common.context.annotation.context.Dependence;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.service.IJSONStringConverterPro;
import pro.jk.ejoker.eventing.IDomainEvent;
import pro.jk.ejoker.eventing.IEventSerializer;
import pro.jk.ejoker.infrastructure.ITypeNameProvider;

@EService
public class EventStreamMongoSerializer implements IEventSerializer {

	@Dependence
	private IJSONStringConverterPro jsonSerializer;
	
	@Dependence
	private ITypeNameProvider typeNameProvider;

	@Suspendable
	@Override
	public Map<String, String> serializer(Collection<IDomainEvent<?>> events) {
		Map<String, String> dict = new LinkedHashMap<String, String>();
		for(IDomainEvent<?> event:events)
			dict.put(typeNameProvider.getTypeName(event.getClass()).replaceAll("\\.", "_"), jsonSerializer.convert(event));
		return dict;
	}

	@Suspendable
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
