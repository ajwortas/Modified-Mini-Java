package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class Operator extends Terminal{

	//public TypeDenoter opType;
	
	public Operator(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitOperator(this);
	}

}
