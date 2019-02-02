package pro.jiefzz.ejoker.test.javaGenericExpression.testC;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiefzz.ejoker.domain.IAggregateRoot;
import com.jiefzz.ejoker.eventing.DomainEventStream;
import com.jiefzz.ejoker.eventing.IDomainEvent;

import pro.jiefzz.ejoker.test.javaGenericExpression.testA.C;

public class GenericType <L extends List<String>, T extends C<String, Double> & IAggregateRoot> {
	//泛型数组
	@SuppressWarnings({ "unused", "unchecked" })
	private Class<? extends Number>[] numberTypes = new Class[10];
	
	@SuppressWarnings({ "unused" })
	private List<String> list = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private Map<String, ? extends Number> numberList = new HashMap<>();
	
	//带泛型类参数的方法
	public List<String> getList(Map<String, Object> map, List<Integer> list) {
		return null;
	}
	
	static class Foo extends C<String, Double> implements IAggregateRoot, Interface2 {

		private static final long serialVersionUID = -1584590426032810695L;

		@Override
		public long getVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getUniqueId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IDomainEvent<?>> getChanges() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void acceptChanges(long newVersion) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void replayEvents(Collection<DomainEventStream> eventStreams) {
			// TODO Auto-generated method stub
			
		}}
	
	@SuppressWarnings("rawtypes")
	public static void showTypeVariables() {
		Class<GenericType> clazz = GenericType.class;
		//这个方法在GenericDeclaration中定义，用于获取类型变量
		TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
		System.out.println("打印" + clazz.getSimpleName() + "定义的参数类型：");
		for (TypeVariable<?> param : typeParameters) {
			System.out.println("参数名：" + param.getName());
			System.out.println("参数上界列表：" + Arrays.asList(param.getBounds()));
		}
		System.out.println();
	}
	
	public static void showParameterType() {
		//这里，我用list数据域展示ParameterizedType API
		try {
			Field field = GenericType.class.getDeclaredField("list");
			//注意，一定要使用带Generic前缀的方法
			Type fieldType = field.getGenericType();
			System.out.println("ParameterizedType: " + fieldType);
			if (fieldType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)fieldType;
				Type actualType = pt.getActualTypeArguments()[0];
				System.out.println(actualType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public static void showWildcardType() {
		try {
			Field field = GenericType.class.getDeclaredField("numberList");
			ParameterizedType fieldType = (ParameterizedType)field.getGenericType();
			Type[] actualTypeArguments = fieldType.getActualTypeArguments();
			for(int i=0; i<actualTypeArguments.length; i++) {
				Type type = fieldType.getActualTypeArguments()[i];
				WildcardType wt = (WildcardType)type;
				System.out.println("WildcardType upperBounds:" + Arrays.asList(wt.getUpperBounds()));
				System.out.println(type.getClass().getName());
				System.out.println(type instanceof ParameterizedType);
				System.out.println("====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public static void showGenericArrayType() {
		try {
			Field field = GenericType.class.getDeclaredField("numberTypes");
			GenericArrayType gat = (GenericArrayType)field.getGenericType();
			System.out.println("array component type :" + gat.getGenericComponentType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		showTypeVariables();
		showParameterType();
		showWildcardType();
		showGenericArrayType();
	}

}
