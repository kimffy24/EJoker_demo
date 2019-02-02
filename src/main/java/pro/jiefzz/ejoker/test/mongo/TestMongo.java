package pro.jiefzz.ejoker.test.mongo;

import static com.mongodb.client.model.Updates.addEachToSet;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.currentTimestamp;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.max;
import static com.mongodb.client.model.Updates.min;
import static com.mongodb.client.model.Updates.mul;
import static com.mongodb.client.model.Updates.popFirst;
import static com.mongodb.client.model.Updates.popLast;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.pullAll;
import static com.mongodb.client.model.Updates.pullByFilter;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.pushEach;
import static com.mongodb.client.model.Updates.rename;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.setOnInsert;
import static com.mongodb.client.model.Updates.unset;

import java.util.Arrays;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.UpdateOptions;

public class TestMongo {

	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient("192.168.199.123", 27017);

		/*
		 * 选择数据库
		 */
		MongoDatabase mongoDatabase = mongoClient.getDatabase("dbTest");

		/*
		 * 选择文档集合
		 */
		MongoCollection<Document> mc = mongoDatabase.getCollection("mycollection");
		

		FindIterable<Document> iterable = mc.find(new Document("oop", "java"));
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(Document t) {
				System.err.println(t);
			}
		});
		if(System.currentTimeMillis() > 0)
			return;
		
		mc.insertOne(new Document("oop", "java"));
		printCollection("insert java", mc);
		
		//插入一个包含两个字段的文档
		Document doc = new Document("oop", "csharp").append("copyright", "microsoft");
		mc.insertOne(doc);
		printCollection("insert csharp", mc);
		
		//查找并修改一个文档
		mc.findOneAndReplace(new Document("oop", "java"), new Document("oop", "java").append("copyright", "oracle"));
		printCollection("findAndReplace java", mc);
		
		//删除一个文档
		mc.deleteOne(new Document("oop", "java"));
		printCollection("delete java", mc);
		
		//删除全部文档
		mc.deleteMany(new Document());
		printCollection("delete all", mc);
		
		//重新插入测试文档
		mc.insertOne(new Document("oop", "java").append("copyright", "oracle"));
		mc.insertOne(new Document("oop", "csharp").append("copyright", "microsoft"));
		printCollection("insert java,csharp and swift", mc);
		
		//$set 文档中存在指定字段则修改,没有则添加
		mc.updateMany(new Document(), set("rank", 100));
		printCollection("$set all rank 100", mc);
		
		//$unset 文档中存在指定字段则删除该字段
		mc.updateOne(new Document("oop", "csharp"), unset("rank"));
		printCollection("unset csharp rank", mc);
		
		//$inc 文档中存在指定字段则相加,没有则添加
		mc.updateOne(new Document("oop", "csharp"), inc("rank", 30));
		printCollection("$inc csharp rank 30", mc);
		mc.updateOne(new Document("oop", "csharp"), inc("rank", 31));
		printCollection("$inc csharp rank 31", mc);
		
		//$setOnInsert 在更新时指定upsert=true并实际触发了插入操作时生效
		mc.updateOne(new Document("oop", "swift").append("copyright", "apple"), setOnInsert("rank", 100), new UpdateOptions().upsert(true));
		printCollection("$setOnInsert rank 100 for swift", mc);
		
		//$mul 相乘
		mc.updateOne(new Document("oop", "java"), mul("rank", 0.2));
		printCollection("$mul java rank: 0.2", mc);
		
		//$rename 重命名
		mc.updateMany(new Document(), rename("rank", "ranks"));
		printCollection("$rename all rank to ranks", mc);
		
		//$min 取当前值和指定值之间比较小的
		mc.updateMany(new Document(), min("ranks", 50));
		printCollection("$min all ranks: 50", mc);
		
		//$max 取当前值和指定值之间比较大的
		mc.updateMany(new Document(), max("ranks", 40));
		printCollection("$max all ranks: 40", mc);
		
		//$currentDate
		mc.updateMany(new Document("oop", "java"), currentDate("add"));
		printCollection("$currentDate java", mc);
		
		//$currentTimestamp
		mc.updateMany(new Document("oop", "java"), currentTimestamp("lastModified"));
		printCollection("$currentTimestamp java", mc);
		
		//$addToSet 添加一个元素到不重复集合
		mc.updateMany(new Document("oop", "java"), addToSet("keywords", "for"));
		mc.updateMany(new Document("oop", "java"), addToSet("keywords", "for"));
		printCollection("$addToSet java keywords: for", mc);
		
		//$addEachToSet 添加一组元素到不重复集合
		mc.updateMany(new Document("oop", "java"), addEachToSet("keywords", Arrays.asList("while", "true", "do", "new", "override")));
		mc.updateMany(new Document("oop", "java"), addEachToSet("keywords", Arrays.asList("while", "true", "do", "new", "override")));
		printCollection("$addEachToSet java keywords: while,true,do,new,override", mc);
		
		//$popFirst 删除第一个元素
		mc.updateMany(new Document("oop", "java"), popFirst("keywords"));
		printCollection("$popFirst java keywords", mc);
		
		//$popLast 删除最后一个元素
		mc.updateMany(new Document("oop", "java"), popLast("keywords"));
		printCollection("$popLast java keywords", mc);
		
		//$pull 删除指定元素
		mc.updateMany(new Document("oop", "java"), pull("keywords", "new"));
		printCollection("$pull java keywords: new", mc);
		
		//$pullByFilter 根据Filters删除
		mc.updateMany(new Document("oop", "java"), pullByFilter(Filters.gte("keywords", "true")));
		printCollection("$pullByFilter java keywords: true", mc);
		
		//$pullAll 删除一组元素
		mc.updateMany(new Document("oop", "java"), pullAll("keywords", Arrays.asList("while", "true", "do", "new", "override")));
		printCollection("$pullAll java keywords", mc);
 
		//$push 添加一个元素到可重复集合
		mc.updateMany(new Document("oop", "java"), push("scores", 89));
		printCollection("$push java scores: 89", mc);
		
		//$pushEach 添加一组元素到可重复集合
		mc.updateMany(new Document("oop", "java"), pushEach("scores", Arrays.asList(89, 90, 92)));
		printCollection("$pushEach java scores: 89,90,92", mc);
		
		//在集合的指定位置插入一组元素
		mc.updateMany(new Document("oop", "java"), pushEach("scores", Arrays.asList(11, 12, 13), new PushOptions().position(0)));
		printCollection("$pushEach java scores: 11,12,13 at position 0", mc);
		
		//在集合的指定位置插入一组元素并倒序排列
		mc.updateMany(new Document("oop", "java"), pushEach("scores", Arrays.asList(40, 41), new PushOptions().sort(-1)));
		printCollection("$pushEach java scores: 40,41 and sort(-1)", mc);
		
		//在集合的指定位置插入一组元素, 倒序排列后保留前3个
		mc.updateMany(new Document("oop", "java"), pushEach("scores", Arrays.asList(60, 61), new PushOptions().sort(-1).slice(3)));
		printCollection("$pushEach java scores: 60,61 and sort(-1) and slice(3)", mc);
		
		//插入一组内嵌文档
		Bson bson = pushEach("experts",
                Arrays.asList(new Document("first", "Rod").append("last", "Johnson"),
                              new Document("first", "Doug").append("last", "Cutting")));
		mc.updateOne(new Document("oop", "java"), bson);
		printCollection("$pushEach", mc);
		
		//combine 组合Bson
		bson = combine(set("author", "James Gosling"), set("version", "8.0"));
		mc.updateOne(new Document("oop", "java"), bson);
		printCollection("$combine", mc);

	}

	// 打印查询的结果集
	public static void printCollection(String doing, MongoCollection<Document> mc) {
		System.out.println(doing);
		FindIterable<Document> iterable = mc.find();
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
		System.out.println("------------------------------------------------------");
		System.out.println();
	}

}
