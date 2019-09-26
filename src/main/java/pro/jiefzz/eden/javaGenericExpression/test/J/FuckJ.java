package pro.jiefzz.eden.javaGenericExpression.test.J;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pro.jiefzz.eden.javaGenericExpression.test.A.Z;
import pro.jiefzz.ejoker.z.utils.relationship.IRelationshipTreeAssemblers;
import pro.jiefzz.ejoker.z.utils.relationship.RelationshipTreeUtil;

public class FuckJ {

	
	public static void main(String[] args) {
		
		RelationshipTreeUtil<JSONObject, JSONArray> relationshipTreeUtil = new RelationshipTreeUtil<>(new IRelationshipTreeAssemblers<JSONObject, JSONArray>() {
			@Override
			public JSONObject createKeyValueSet() {
				return new JSONObject();
			}

			@Override
			public JSONArray createValueSet() {
				return new JSONArray();
			}

			@Override
			public boolean isHas(JSONObject targetNode, Object key) {
				return targetNode.containsKey(key);
			}

			@Override
			public void addToValueSet(JSONArray valueSet, Object child) {
				valueSet.add(child);
			}

			@Override
			public void addToKeyValueSet(JSONObject keyValueSet, Object child, String key) {
				keyValueSet.put(key, child);
			}
		});

		JB1 jb1 = new JB1();
		
		JB2 jb2 = new JB2();
		
		
		jb1.addValue("鸡巴飞");
		jb1.setVarA1("牛逼的程序猿");

		jb2.setVarA1(new Z());
		jb2.addValue(new Z());
		jb2.addValue(new Z());
		jb2.addValue(new Z());
		
	}
	
	
}
