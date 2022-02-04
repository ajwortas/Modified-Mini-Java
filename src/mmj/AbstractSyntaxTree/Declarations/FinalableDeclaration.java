package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class FinalableDeclaration extends Declaration {
	public final boolean isFinal;
	public FinalableDeclaration(String n, TypeDenoter t, boolean isFinal, SourcePosition sp) {
		super(n, t, sp);
		this.isFinal=isFinal;
	}
}
