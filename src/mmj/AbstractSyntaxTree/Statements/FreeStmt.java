package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.SyntacticAnalyzer.SourcePosition;

public class FreeStmt extends Statement{
	public final Reference ref;
	public FreeStmt(Reference ref, SourcePosition sp) {
		super(sp);
		this.ref=ref;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitFreeStatement(this);
		
	}
}
