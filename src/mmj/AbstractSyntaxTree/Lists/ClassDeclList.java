package mmj.AbstractSyntaxTree.Lists;

import java.util.Comparator;

import mmj.AbstractSyntaxTree.Declarations.ClassDecl;

public class ClassDeclList extends AbstractASTList<ClassDecl>{
	public ClassDeclList() {
		super();
	}
	public void sort() {
		list.sort(new CompareClasses());
	}
	private class CompareClasses implements Comparator<ClassDecl>{
		@Override
		public int compare(ClassDecl arg0, ClassDecl arg1) {
			return arg0.classID-arg1.classID;
		}
	}
}
