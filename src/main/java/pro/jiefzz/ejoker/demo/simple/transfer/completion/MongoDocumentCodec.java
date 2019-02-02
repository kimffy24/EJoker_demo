package pro.jiefzz.ejoker.demo.simple.transfer.completion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.DomainEventStream;
import com.jiefzz.ejoker.eventing.IEventSerializer;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.utils.relationship.IRelationshipTreeAssemblers;
import com.jiefzz.ejoker.z.common.utils.relationship.IRelationshipTreeDisassemblers;
import com.jiefzz.ejoker.z.common.utils.relationship.RelationshipTreeRevertUtil;
import com.jiefzz.ejoker.z.common.utils.relationship.RelationshipTreeUtil;
import com.jiefzz.ejoker.z.common.utils.relationship.SpecialTypeCodec;
import com.jiefzz.ejoker.z.common.utils.relationship.SpecialTypeCodecStore;

//@EService
public class MongoDocumentCodec {

	private final static Logger logger = LoggerFactory.getLogger(MongoDocumentCodec.class);
	
	@Dependence
	IEventSerializer eventSerializer;

	private SpecialTypeCodecStore<String> specialTypeHandler;
	
	private RelationshipTreeUtil<Document, ArrayList> relationshipTreeUtil;

	private RelationshipTreeRevertUtil<Document, ArrayList> revertRelationshipTreeUitl;
	
	@SuppressWarnings("unchecked")
	public MongoDocumentCodec() {
		specialTypeHandler = new SpecialTypeCodecStore<String>()
				.append(BigDecimal.class, new SpecialTypeCodec<BigDecimal, String>(){

					@Override
					public String encode(BigDecimal target) {
						return target.toPlainString();
					}

					@Override
					public BigDecimal decode(String source) {
						return new BigDecimal(source);
					}
					
				})
				.append(BigInteger.class, new SpecialTypeCodec<BigInteger, String>(){

					@Override
					public String encode(BigInteger target) {
						return target.toString();
					}

					@Override
					public BigInteger decode(String source) {
						return new BigInteger(source);
					}
					
				})
				.append(char.class, new SpecialTypeCodec<Character, String>(){

					@Override
					public String encode(Character target) {
						return "" + (int )target.charValue();
					}

					@Override
					public Character decode(String source) {
						return (char )Integer.parseInt(source);
					}
					
				})
				.append(Character.class, new SpecialTypeCodec<Character, String>(){

					@Override
					public String encode(Character target) {
						return "" + (int )target.charValue();
					}

					@Override
					public Character decode(String source) {
						return (char )Integer.parseInt(source);
					}
					
				})
				.append(ObjectId.class, new SpecialTypeCodec<ObjectId, ObjectId>() {
					@Override
					public ObjectId encode(ObjectId target) {
						return target;
					}
					@Override
					public ObjectId decode(ObjectId source) {
						return source;
					}
				})
				;
		
		relationshipTreeUtil = new RelationshipTreeUtil<Document, ArrayList>(new IRelationshipTreeAssemblers<Document, ArrayList>() {
			
			@Override
			public Document createKeyValueSet() {
				return new Document();
			}

			@Override
			public ArrayList createValueSet() {
				return new ArrayList();
			}

			@Override
			public boolean isHas(Document targetNode, Object key) {
				return targetNode.containsKey(key);
			}

			@Override
			public void addToValueSet(ArrayList valueSet, Object child) {
				valueSet.add(child);
			}

			@Override
			public void addToKeyValueSet(Document keyValueSet, Object child, String key) {
				keyValueSet.append(key, child);
			}

				}, specialTypeHandler);
		
		revertRelationshipTreeUitl = new RelationshipTreeRevertUtil<Document, ArrayList>(new IRelationshipTreeDisassemblers<Document, ArrayList>() {

			@Override
			public boolean hasKey(Document source, Object key) {
				return source.containsKey(key);
			}
			
			@Override
			public Object getValue(Document source, Object key) {
				return source.get(key);
			}

			@Override
			public Object getValue(ArrayList source, int index) {
				return source.get(index);
			}

			@Override
			public int getVPSize(ArrayList source) {
				return source.size();
			}

			@Override
			public Set getKeySet(Document source) {
				return new HashSet<>(source.keySet());
			}
				}, specialTypeHandler);
	}
	
	public <T> Document convert(DomainEventStream object) {
		return relationshipTreeUtil.getTreeStructure(object);
	}
	
	public DomainEventStream revert(Document doc) {
		return revertRelationshipTreeUitl.revert(doc, DomainEventStream.class);
	}
	
}
