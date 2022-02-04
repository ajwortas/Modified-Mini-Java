package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class LocalDecl extends FinalableDeclaration {

	public LocalDecl(String n, TypeDenoter t, boolean isFinal, SourcePosition sp) {
		super(n, t, isFinal, sp);
	}


}
