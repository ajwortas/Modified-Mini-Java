package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.SyntacticAnalyzer.SourcePosition;

public class CallStmt extends Statement{

	public final Reference ref;
	
	public CallStmt(Reference reference, SourcePosition sp) {
		super(sp);
		ref=reference;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitCallStatement(this);
		
	}

}
