package mmj.AbstractSyntaxTree.TypeDenoters;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class TypeDenoter extends ASTComponent {

	public final TypeKind kind;
	
	public TypeDenoter(TypeKind tk, SourcePosition sp) {
		super(sp);
		kind=tk;
	}

}
