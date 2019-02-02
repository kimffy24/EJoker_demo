//package pro.jiefzz.ejoker.test.treeSerializer;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.Random;
//
//import com.jiefzz.ejoker.EJoker;
//import com.jiefzz.ejoker.z.common.context.IEJokerContext;
//import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
//import com.jiefzz.ejoker.z.common.utils.relationship.IRelationshipTreeAssemblers;
//
//import net.minidev.json.JSONArray;
//import net.minidev.json.JSONObject;
//import pro.jiefzz.test.Person;
//
//public class Main1 {
//
//	private final static IRelationshipTreeAssemblers th = new IRelationshipTreeAssemblers<JSONObject, JSONArray>() {
//
//		public JSONArray createValueSet() {
//			return new JSONArray();
//		}
//
//		public boolean isHas(JSONObject targetNode, Object key) {
//			return targetNode.containsKey(key);
//		}
//
//		public void addToValueSet(JSONArray valueSet, Object child) {
//			valueSet.add(child);
//		}
//
//		public void addToKeyValueSet(JSONObject keyValueSet, Object child, String key) {
//			keyValueSet.put(key, child);
//		}
//
//		@Override
//		public JSONObject createKeyValueSet() {
//			return new JSONObject();
//		}
//		
//	};
//
//	public static void main(String[] args) throws Exception {
//
//		UnlimitedRelationshipTreeUtil<JSONObject, JSONArray> relationshipTreeUtil = new UnlimitedRelationshipTreeUtil<>(
//				th);
//		;
//
//		EJoker eJokerInstance = EJoker.getInstance();
//		IEJokerSimpleContext eJokerContext = eJokerInstance.getEJokerContext();
//
//		{
//			IEJokerContext eJokerFullContext = (IEJokerContext) eJokerContext;
//			eJokerFullContext.scanPackageClassMeta("pro.jiefzz.test");
//		}
//
//		Person toperFather = new Person(1);
//		Person toperMother = new Person(0);
//
//		gereneal(toperFather, toperMother);
//
//		JSONObject processKVP = relationshipTreeUtil.processKVP(toperFather);
//
//		System.err.println(0);
////		ObjectMapper oMapper = new ObjectMapper();
////		String writeValueAsString = oMapper.writeValueAsString(toperFather);
////		System.err.println(1);
////		System.err.println(writeValueAsString);
//		System.err.println(processKVP.toJSONString());
//
//	}
//
//	public final static int numberOfGeneral = 30;
//	public static int nowGeneral = 0;
//
//	public static void gereneal(Person p1, Person p2) {
//		if (nowGeneral++ >= numberOfGeneral)
//			return;
//		Random random = new Random();
//		int nextInt = random.nextInt(5) + 1;
//
//		Person father = p1.sex == 1 ? p1 : p2;
//
//		for (int x = 0; x < nextInt; x++) {
//			p1.general(p2, 1);
//		}
//		for (Person child : father.children) {
//			int correct = child.sex == 0 ? 1 : 0;
//			int uncorrect = child.sex == 0 ? 0 : 1;
//			gereneal(child,
//					new Person(((Number) (System.currentTimeMillis() % 5)).intValue() != 0 ? correct : uncorrect));
//		}
//	}
//
//	public static String getTrace(Throwable t) {
//		StringWriter stringWriter = new StringWriter();
//		PrintWriter writer = new PrintWriter(stringWriter);
//		t.printStackTrace(writer);
//		StringBuffer buffer = stringWriter.getBuffer();
//		return buffer.toString();
//	}
//}
