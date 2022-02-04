package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.StatementList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class BlockStmt extends Statement{

	public final StatementList stmtList;
	
	public BlockStmt(StatementList sl, SourcePosition sp) {
		super(sp);
		stmtList=sl;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitBlockStatement(this);
	}

}
