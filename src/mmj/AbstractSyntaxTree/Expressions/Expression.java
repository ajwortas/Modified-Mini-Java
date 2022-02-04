package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class Expression extends ASTComponent {

	public TypeDenoter evaluatedType;
	public Expression(SourcePosition sp) {
		super(sp);
	}

}
