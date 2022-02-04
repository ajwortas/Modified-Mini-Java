package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class CallRef extends NestedReference {
	public final ExpressionList argList;
	public CallRef(Reference nested, ExpressionList argL, SourcePosition sp) {
		super(nested, sp);
		argList=argL;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitCallReference(this);

	}
}
