package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public class BreakStmt extends Statement {

	public BreakStmt(SourcePosition sp) {
		super(sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitBreakStatement(this);
	}
	
}
