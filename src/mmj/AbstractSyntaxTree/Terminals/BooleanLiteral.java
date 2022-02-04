package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class BooleanLiteral extends Terminal{

	public BooleanLiteral(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitBooleanLiteral(this);
	}

}
