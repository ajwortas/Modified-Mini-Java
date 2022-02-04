package mmj.AbstractSyntaxTree.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractASTList<Type>  implements Iterable<Type> {
	protected List<Type> list;	
	public AbstractASTList() {
		list = new ArrayList<Type>();
	}   

	public void add(Type item){
		list.add(item);
	}

	public Type get(int i){
		return list.get(i);
	}

	public int size() {
		return list.size();
	}

	public Iterator<Type> iterator() {
		return list.iterator();
	}
}
