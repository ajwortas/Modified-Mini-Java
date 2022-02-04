package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class IntegerLiteral extends Terminal{
	public IntegerLiteral(Token t) {
		super(t);
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitIntegerLiteral(this);
	}
}
