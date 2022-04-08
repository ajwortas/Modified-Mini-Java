package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ContinueStmt extends Statement{
	
	public ContinueStmt(SourcePosition sp) {
		super(sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitContinueStatement(this);
	}

}
