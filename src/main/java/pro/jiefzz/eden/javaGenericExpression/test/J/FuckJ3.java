package pro.jiefzz.eden.javaGenericExpression.test.J;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pro.jiefzz.eden.javaGenericExpression.test.A.Y;
import pro.jiefzz.ejoker.z.utils.relationship.IRelationshipTreeDisassemblers;
import pro.jiefzz.ejoker.z.utils.relationship.RelationshipTreeRevertUtil;
import pro.jiefzz.ejoker.z.utils.relationship.SpecialTypeCodec;
import pro.jiefzz.ejoker.z.utils.relationship.SpecialTypeCodecStore;

public class FuckJ3 {

	static RelationshipTreeRevertUtil<JSONObject, JSONArray> relationshipTreeRevertUtil = new RelationshipTreeRevertUtil<>(new IRelationshipTreeDisassemblers<JSONObject, JSONArray>(){

		@Override
		public Object getValue(JSONObject source, Object key) {
			return source.get(key);
		}

		@Override
		public Object getValue(JSONArray source, int index) {
			return source.get(index);
		}

		@Override
		public int getVPSize(JSONArray source) {
			return source.size();
		}

		@Override
		public Set getKeySet(JSONObject source) {
			return source.keySet();
		}

		@Override
		public boolean hasKey(JSONObject source, Object key) {
			return false;
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
		
		String target = "{\"container\":[{\"1536917501282\":{\"chushizhi\":998,\"eValue\":\"FIRST\"}},{\"1536917500280\":{\"chushizhi\":108,\"eValue\":\"SECOND\"},\"1536917502282\":{\"chushizhi\":9527,\"eValue\":\"FIRST\"}}],\"important\":{\"fuckMap\":{\"1\":\"什么听说，是事实！！！\"},\"i\":1,\"varA1\":\"听说你很牛逼哦！？\"},\"testTuple2\":[{\"ttt\":{\"chushizhi\":80808,\"eValue\":\"FIRST\"},\"aaaa\":[[1,3,5,7],[2,4,8,16,32,64]]},{\"ttt\":{\"chushizhi\":71717,\"eValue\":\"FIRST\"},\"aaaa\":[[1,3,5,7],[2,4,8,16,32,64]]}],\"testBD\":\"3.1415926\",\"testTuple\":[{\"age\":\"25\",\"ffff\":5555},null]}";
		
		
		JSONObject resultJO1;
		JSONObject resultJO2 = JSONObject.parseObject(target);

//		resultJO1 = FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
		relationshipTreeRevertUtil.revert(resultJO2, JDa2.class);
		
		CountDownLatch cdl = new CountDownLatch(1000);
		final AtomicInteger ai = new AtomicInteger(0);
		long[] results = new long[1000];
		
		Thread[] threads = new Thread[] {
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
				new Thread(() ->  {
					for( ;; ) {
						int index = ai.getAndIncrement();
						if(index>=1000)
							return;
						long t = System.currentTimeMillis();
						FuckJ2.relationshipTreeUtil.getTreeStructure(jDa2);
						results[index] = System.currentTimeMillis() - t;
						cdl.countDown();
					}
				}),
		};
		
		for(int j=0; j<threads.length; j++)
			threads[j].start();
		
		cdl.await();
		
		for(long result:results)
			System.err.println(result);
	}

}
