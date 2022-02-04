package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Declarations.VarDecl;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.AbstractSyntaxTree.Expressions.RangeExpr;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ForStmt extends LoopStmt {
	public final Statement init, incr;
	public final RangeExpr range;
	public final VarDecl var;
	public ForStmt(VarDecl var, RangeExpr range, Statement b, SourcePosition sp) {
		super(null, b, sp);
		init=null;
		incr=null;
		this.range=range;
		this.var=var;
	}
	public ForStmt(Statement initial, Expression cond, Statement increment, Statement b, SourcePosition sp) {
		super(cond, b, sp);
		init=initial;
		incr=increment;
		range=null;
		var=null;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitForStatement(this);
	}
}
