package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.Lists.ExpressionListList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class BarrierStmt extends Statement {

	public final ExpressionListList lambdas;
	public final ExpressionList arrAndParams;
	
	public BarrierStmt(ExpressionList arrAndParams, SourcePosition sp) {
		super(sp);
		this.arrAndParams=arrAndParams;
		lambdas=null;
	}

	public BarrierStmt(ExpressionListList lambdas, SourcePosition sp) {
		super(sp);
		arrAndParams=null;
		this.lambdas=lambdas;
	}
	
	@Override
	public void visit(ASTVisitor v) {
		v.visitBarrierStmt(this);
	}

}
