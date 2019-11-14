package pro.jiefzz.eden.treeSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.common.utils.relationship.IRelationshipTreeAssemblers;
import pro.jiefzz.ejoker.common.utils.relationship.IRelationshipTreeDisassemblers;
import pro.jiefzz.ejoker.common.utils.relationship.RelationshipTreeRevertUtil;
import pro.jiefzz.ejoker.common.utils.relationship.RelationshipTreeUtil;
import pro.jiefzz.ejoker.common.utils.relationship.SpecialTypeCodecStore;

public class StandardConverter {

	private final static Logger logger = LoggerFactory.getLogger(StandardConverter.class);

	private SpecialTypeCodecStore<String> specialTypeHandler;
	
	private RelationshipTreeUtil<Map<String, Object>, List<Object>> relationshipTreeUtil;

	private RelationshipTreeRevertUtil<Map<String, Object>, List<Object>> revertRelationshipTreeUitl;
	
	public StandardConverter() {
		specialTypeHandler = null;
		
		relationshipTreeUtil = new RelationshipTreeUtil<Map<String, Object>, List<Object>>(new IRelationshipTreeAssemblers<Map<String, Object>, List<Object>>(){

			@Override
			public Map<String, Object> createKeyValueSet() {
				return new HashMap<String, Object>();
			}

			@Override
			public List<Object> createValueSet() {
				return new ArrayList<Object>();
			}

			@Override
			public boolean isHas(Map<String, Object> targetNode, Object key) {
				return targetNode.containsKey((String )key);
			}

			@Override
			public void addToValueSet(List<Object> valueSet, Object child) {
				valueSet.add(child);
			}

			@Override
			public void addToKeyValueSet(Map<String, Object> keyValueSet, Object child, String key) {
				keyValueSet.put(key, child);
			}

			
		});
		
		revertRelationshipTreeUitl = new RelationshipTreeRevertUtil<Map<String, Object>, List<Object>>(new IRelationshipTreeDisassemblers<Map<String, Object>, List<Object>>(){

			@Override
			public Object getValue(Map<String, Object> source, Object key) {
				return source.get(key);
			}

			@Override
			public Object getValue(List<Object> source, int index) {
				return source.get(index);
			}

			@Override
			public int getVPSize(List<Object> source) {
				return source.size();
			}

			@Override
			public Set getKeySet(Map<String, Object> source) {
				return source.keySet();
			}

			@Override
			public boolean hasKey(Map<String, Object> source, Object key) {
				return false;
			}
			
		});
	}

	public <T> Map<String, Object> convert(T object) {
		return relationshipTreeUtil.getTreeStructure(object);
	}
	
	public <T> T revert(Map<String, Object> input, Class<T> clazz) {
		return revertRelationshipTreeUitl.revert(input, clazz);
	}
}
