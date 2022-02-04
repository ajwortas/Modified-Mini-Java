package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

public class IfStmt extends Statement{
	public final Expression cond;
	public final Statement ifTrue, ifFalse;
	public IfStmt(Expression cond, Statement ifTrue, Statement ifFalse, SourcePosition sp) {
		super(sp);
		this.cond=cond;
		this.ifTrue=ifTrue;
		this.ifFalse=ifFalse;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitIfStatement(this);	
	}

}
