package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class NullLiteral extends Terminal{

	public NullLiteral(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitNullLiteral(this);
	}

}
