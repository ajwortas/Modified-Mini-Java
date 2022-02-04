package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class Assignment extends Terminal {

	public Assignment(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitAssignment(this);
	}

}
