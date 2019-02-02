package pro.jiefzz.ejoker.test.treeSerializer;

import java.util.ArrayList;
import java.util.List;

public class Main3 {

	public static void main(String[] args) {
		
		List<Object> arrayList = new ArrayList<>();
		arrayList.add(1);
		arrayList.add(2d);
		
		List<Object> ax = new ArrayList<>();
		ax.add(1);
		ax.add(2d);
		ax.add(3d);
		ax.add(24d);

		int a = 0;
		int b = 0;
		
		if( 0 > (a = 5-ax.size()-2) || 2 == (b = arrayList.size())) {
			System.err.println("ok");
		}
		
		System.err.println("a = " + a);
		System.err.println("b = " + b);
	
	}

}
