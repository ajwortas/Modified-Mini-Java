package mmj.AbstractSyntaxTree.Lists;

import java.util.Comparator;

import mmj.AbstractSyntaxTree.Declarations.FieldDecl;

public class FieldDeclList extends AbstractASTList<FieldDecl>{
	public FieldDeclList() {
		super();
	}
	public void sort() {
		list.sort(new CompareMethods());
	}
	private class CompareMethods implements Comparator<FieldDecl>{
		@Override
		public int compare(FieldDecl arg0, FieldDecl arg1) {
			return arg0.fieldID-arg1.fieldID;
		}
	}
}
