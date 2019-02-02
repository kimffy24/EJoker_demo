package pro.jiefzz.ejoker.test.javaGenericExpression.testJ;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiefzz.ejoker.z.common.utils.relationship.IRelationshipTreeAssemblers;
import com.jiefzz.ejoker.z.common.utils.relationship.RelationshipTreeUtil;
import com.jiefzz.ejoker.z.common.utils.relationship.SpecialTypeCodec;
import com.jiefzz.ejoker.z.common.utils.relationship.SpecialTypeCodecStore;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.Y;

public class FuckJ2 {

	static RelationshipTreeUtil<JSONObject, JSONArray> relationshipTreeUtil = new RelationshipTreeUtil<>(new IRelationshipTreeAssemblers<JSONObject, JSONArray>() {
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
	}, new SpecialTypeCodecStore<String>() {
		{
			append(BigDecimal.class, new SpecialTypeCodec<BigDecimal, String>() {

				@Override
				public String encode(BigDecimal target) {
					return target.toString();
				}

				@Override
				public BigDecimal decode(String source) {
					return new BigDecimal(source);
				}
				
			});
		}
	});
	
	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() throws Exception {
			
		JDa2 jDa2 = new JDa2();
		
		jDa2.addValue(new Y<>(108));
		TimeUnit.SECONDS.sleep(1);
		jDa2.addValue(new Y<>(998));
		TimeUnit.SECONDS.sleep(1);
		jDa2.addValue(new Y<>(9527));
		
		jDa2.addValueToImportant("什么听说，是事实！！！");
		
		jDa2.testTuple[0] = new TestTuple();
		jDa2.testTuple[0].ffff = 5555;
		jDa2.testTuple[0].age = "25";
		
		jDa2.addTestTuple2(new TestTuple2<>(new Y<>(80808)));
		jDa2.addTestTuple2(new TestTuple2<>(new Y<>(71717)));
		
		convert(jDa2);
		
	}
	
	public static final void convert(Object obj) {
		JSONObject treeStructure = relationshipTreeUtil.getTreeStructure(obj);
		System.err.println(treeStructure);
	}
}
