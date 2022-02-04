package mmj.AbstractSyntaxTree.Lists;

import java.util.Comparator;

import mmj.AbstractSyntaxTree.Declarations.MethodDecl;

public class MethodDeclList extends AbstractASTList<MethodDecl>{
	public MethodDeclList() {
		super();
	}
	public void sort() {
		list.sort(new CompareMethods());
	}
	private class CompareMethods implements Comparator<MethodDecl>{
		@Override
		public int compare(MethodDecl arg0, MethodDecl arg1) {
			return arg0.mt.methodID-arg1.mt.methodID;
		}
	}
}
