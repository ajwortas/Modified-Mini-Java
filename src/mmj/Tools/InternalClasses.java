package mmj.Tools;

import mmj.AbstractSyntaxTree.Declarations.ClassDecl;
import mmj.AbstractSyntaxTree.Declarations.FieldDecl;
import mmj.AbstractSyntaxTree.Declarations.MethodDecl;

public class InternalClasses {
	private final static ClassDecl [] internalClasses;
	static {
		internalClasses = new ClassDecl[4];
		
		
	}
	
	public static ClassDecl [] getInternalClasses() {
		return internalClasses;
	}
}
