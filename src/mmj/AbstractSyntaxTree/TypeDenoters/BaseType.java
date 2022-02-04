package mmj.AbstractSyntaxTree.TypeDenoters;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.SyntacticAnalyzer.SourcePosition;

public class BaseType extends TypeDenoter {

	public BaseType(TypeKind tk, SourcePosition sp) {
		super(tk, sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitBaseType(this);
	}

}
