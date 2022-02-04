package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.Terminal;
import mmj.SyntacticAnalyzer.SourcePosition;

public class LiteralExpr extends Expression{

	public final Terminal lit;
	
	public LiteralExpr(Terminal t,SourcePosition sp) {
		super(sp);
		lit=t;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitLiteralExpression(this);
		
	}

}
