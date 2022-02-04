package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class StringLiteral extends Terminal{

	public StringLiteral(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitStringLiteral(this);
	}

}
